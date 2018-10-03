const prompt = require('inquirer').createPromptModule();
const questions = require('../questions_base');
const properties = require('properties');
const fs = require('fs');
const yml = require('js-yaml');
const setupBroker = require('../amqpTools');
const path = require("path");


let dockerComposeHinc = {
    version: '3',
    services:{}
}

let exchanges = {};
let localConfigs = [];

prompt(questions).then((ans) => {
    clearResult()

    ans.local_count = parseInt(ans.local_count);
    ans.provider_count = parseInt(ans.provider_count);  
    
    console.log('parameters set :')
    console.log(JSON.stringify(ans, null, 2));
    create(ans);

    setupBroker(exchanges, ans.broker_uri);
    console.log(dockerComposeHinc)
    console.log(yml.safeDump(dockerComposeHinc))
    fs.writeFileSync(path.join(__dirname, "../../result/docker-compose.yml"), yml.safeDump(dockerComposeHinc));
    
});

function create(config){
    createGlobal(config);
    createAllLocals(config);
    createAllAdaptorsAndProviders(config);
    createSoftwareArtefactService(config);
    createInteroperabilityService(config);

    console.log("navigate to the folder ./result and run the command:")
    console.log("$ docker-compose up");
}

function createGlobal(config){
    let globalConfig = require("../configTemplates/global.json");
    globalConfig['spring.rabbitmq.addresses'] = config.broker_uri

    fs.writeFileSync(path.join(__dirname, "../../result/config/global.properties"), properties.stringify(globalConfig));

    let service = {
        image: "rdsea/global",
        ports:[
            "8080:8080"
        ],
        volumes: [
            "./config/global.properties:/application.properties"
        ],
        depends_on:[]
    };

    dockerComposeHinc.services['global'] = service
    exchanges[globalConfig["hinc.global.rabbitmq.input"]] = {
        type: 'fanout',
        queues:[
            globalConfig["hinc.global.rabbitmq.input"]
        ],
    }
}

function createAllLocals(config){
    for(let i=1;i<=config.local_count;i++){
        createLocal(`local${i}`, config);
    }
}

function createLocal(localId, config){
    let localConfig = require("../configTemplates/local.json");
    localConfig["spring.rabbitmq.addresses"] = config.broker_uri;
    localConfig["hinc.local.id"] = localId;
    localConfig["adaptor.amqp.input"] = `adaptor_${localId}_input`;
    localConfig["adaptor.amqp.output.broadcast"] = `adaptor_${localId}_broadcast`;
    localConfig["adaptor.amqp.output.unicast"] = `adaptor_${localId}_unicast`
    fs.writeFileSync(path.join(__dirname, `../../result/config/local.${localId}.properties`), properties.stringify(localConfig));

    let service = {
        image: "rdsea/local",
        volumes: [
            `./config/local.${localId}.properties:/application.properties`
        ],
        depends_on:[
            "global"
        ]
    };

    dockerComposeHinc.services[`local${localId}`] = service;

    exchanges[localConfig["adaptor.amqp.input"]] = {
        type: 'fanout',
        queues:[
            localConfig["adaptor.amqp.input"]
        ],
    }

    localConfigs.push(localConfig);
}

function createAllAdaptorsAndProviders(config){
    localConfigs.forEach((localConfig) => {
        createAdaptorsAndProviders(config, localConfig);
    })
}

function createAdaptorsAndProviders(config, localConfig){
    let adaptorConfigBase = require('../configTemplates/adaptor.json');
    adaptorConfigBase.URI = config.broker_uri;
    adaptorConfigBase.EXCHANGE =  localConfig["adaptor.amqp.input"];
    
    let sensorAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let mqttAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let vesselAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let alarmclientAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));

    sensorAdaptorConfig.ADAPTOR_NAME = `sensor${localConfig["hinc.local.id"]}`;
    mqttAdaptorConfig.ADAPTOR_NAME = `mqtt${localConfig["hinc.local.id"]}`;
    vesselAdaptorConfig.ADAPTOR_NAME = `vessel${localConfig["hinc.local.id"]}`;
    alarmclientAdaptorConfig.ADAPTOR_NAME = `alarmclient${localConfig["hinc.local.id"]}`;

    sensorAdaptorConfig.ENDPOINT = `http://sensorprovider${localConfig["hinc.local.id"]}:3004`;
    mqttAdaptorConfig.ENDPOINT = `http://mqttprovider${localConfig["hinc.local.id"]}:3002`
    vesselAdaptorConfig.ENDPOINT = `http://vesselprovider${localConfig["hinc.local.id"]}:3005`
    alarmclientAdaptorConfig.ENDPOINT = `http://alarmclientprovider${localConfig["hinc.local.id"]}:3006`

    writeAdaptorConfig(sensorAdaptorConfig);
    writeAdaptorConfig(mqttAdaptorConfig);
    writeAdaptorConfig(vesselAdaptorConfig);
    writeAdaptorConfig(alarmclientAdaptorConfig);

    let sensorProviderService = {
        image: "rdsea/sensor-provider-valencia",
    }

    let mqttProviderService = {
        image: "rdsea/mqtt-provider",
    }

    let vesselProviderSevice = {
        image: "rdsea/vessel-provider",
    }

    let alarmclientProviderService = {
        image: "rdsea/alarmclient-provider",
    }

    let sensorAdaptorServce = {
        image: "rdsea/valencia-sensor-adaptor",
        volumes: [
            `./config/${sensorAdaptorConfig.ADAPTOR_NAME}.js:/sensor/config.js`
        ],
    }

    let mqttAdaptorServce = {
        image: "rdsea/mqtt-adaptor",
        volumes: [
            `./config/${mqttAdaptorConfig.ADAPTOR_NAME}.js:/mqtt/config.js`
        ],
    }

    let vesselAdaptorServce = {
        image: "rdsea/vessel-adaptor",
        volumes: [
            `./config/${vesselAdaptorConfig.ADAPTOR_NAME}.js:/vessel/config.js`
        ],
    }

    let alarmclientAdaptorServce = {
        image: "rdsea/alarmclient-adaptor",
        volumes: [
            `./config/${alarmclientAdaptorConfig.ADAPTOR_NAME}.js:/alarmclient/config.js`
        ],
    }

    dockerComposeHinc.services[`sensorprovider${localConfig["hinc.local.id"]}`] = sensorProviderService;
    dockerComposeHinc.services[`mqttprovider${localConfig["hinc.local.id"]}`] = mqttProviderService;
    dockerComposeHinc.services[`vesselprovider${localConfig["hinc.local.id"]}`] = vesselProviderSevice;
    dockerComposeHinc.services[`alarmclientprovider${localConfig["hinc.local.id"]}`] = alarmclientProviderService;

    dockerComposeHinc.services[sensorAdaptorConfig.ADAPTOR_NAME] = sensorAdaptorServce;
    dockerComposeHinc.services[mqttAdaptorConfig.ADAPTOR_NAME] = mqttAdaptorServce;
    dockerComposeHinc.services[vesselAdaptorConfig.ADAPTOR_NAME] = vesselAdaptorServce;
    dockerComposeHinc.services[alarmclientAdaptorConfig.ADAPTOR_NAME] = alarmclientAdaptorServce;

}

function writeAdaptorConfig(adaptorConfig){
    let config = `module.exports = {
        ADAPTOR_NAME : '${adaptorConfig.ADAPTOR_NAME}',
        URI : '${adaptorConfig.URI}',
        EXCHANGE : '${adaptorConfig.EXCHANGE}',
        ENDPOINT: '${adaptorConfig.ENDPOINT}'
    }`

    fs.writeFileSync(path.join(__dirname, `../../result/config/${adaptorConfig.ADAPTOR_NAME}.js`), config);
}


function createInteroperabilityService(config){
    let template = require("../configTemplates/interoperability.json");
    template['GLOBAL_MANAGEMENT_URI'] = "http://global:8080";
    template['SOFTWARE_ARTEFACT_URI'] = "http://software-artefact-service:8082";

    let interoperabilityConfig = `module.exports = {
    SOFTWARE_ARTEFACT_URI:'${template.SOFTWARE_ARTEFACT_URI}',
    GLOBAL_MANAGEMENT_URI:'${template.GLOBAL_MANAGEMENT_URI}',
    SEARCH_ARTEFACTS:'${template.SEARCH_ARTEFACTS}',
    SEARCH_RESOURCES:'${template.SEARCH_RESOURCES}',

    SERVER_PORT:${template.SERVER_PORT},
    MONGODB_URL: '${template.MONGODB_URL}'
}`;

    fs.writeFileSync(path.join(__dirname, "../../result/config/interoperability_config.js"), interoperabilityConfig);

    let service = {
        image: "interoperability-service",
        ports:[
            `8081:${template.SERVER_PORT}`
        ],
        volumes: [
            "./config/interoperability_config.js:/src/config.js"
        ],
        depends_on:[]
    };

    dockerComposeHinc.services['interoperability-service'] = service;
}


function createSoftwareArtefactService(config){

    let service = {
        image: "software-artefact-service",
        ports:[
            "8082:8082"
        ],
        depends_on:[]
    };

    dockerComposeHinc.services['software-artefact-service'] = service;
}


function clearResult(){
    if(fs.existsSync(path.join(__dirname, `../../result`))){
        var deleteFolderRecursive = function(path) {
            if (fs.existsSync(path)) {
                fs.readdirSync(path).forEach(function(file, index){
                var curPath = path + "/" + file;
                if (fs.lstatSync(curPath).isDirectory()) { // recurse
                    deleteFolderRecursive(curPath);
                } else { // delete file
                    fs.unlinkSync(curPath);
                }
                });
                fs.rmdirSync(path);
            }
        };

        deleteFolderRecursive(path.join(__dirname, `../../result`));
    }

    fs.mkdirSync(path.join(__dirname, `../../result`));
    fs.mkdirSync(path.join(__dirname, `../../result/config`));
}
