const prompt = require('inquirer').createPromptModule();
const questions = require('../questions');
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

//prompt(questions).then((ans) => {
{
    clearResult()

    let config = {
        local_count:1,
        provider_count:1,
        broker_uri:"amqp://mgrpueon:5A9m3ovJBL18QQXQQll-hAFnTW9xX5wV@sheep.rmq.cloudamqp.com/mgrpueon",
        database_uri:""
    };

    console.log('parameters set :')
    console.log(JSON.stringify(config, null, 2));
    create(config);

    setupBroker(exchanges, config.broker_uri);
    fs.writeFileSync(path.join(__dirname, "../../result/docker-compose.yml"), yml.safeDump(dockerComposeHinc));
    
}

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
    let globalConfig = require("../configTempates/global.json");
    globalConfig['spring.rabbitmq.addresses'] = config.broker_uri

    fs.writeFileSync(path.join(__dirname, "../../result/config/global.properties"), properties.stringify(globalConfig));

    let service = {
        //TODO use rdsea image
        image: "global-management-service",
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
    let localConfig = require("../configTempates/local.json");
    localConfig["spring.rabbitmq.addresses"] = config.broker_uri;
    localConfig["hinc.local.id"] = localId;
    localConfig["adaptor.amqp.input"] = `adaptor_${localId}_input`;
    localConfig["adaptor.amqp.output.broadcast"] = `adaptor_${localId}_broadcast`;
    localConfig["adaptor.amqp.output.unicast"] = `adaptor_${localId}_unicast`
    fs.writeFileSync(path.join(__dirname, `../../result/config/local.${localId}.properties`), properties.stringify(localConfig));

    let service = {
        //TODO use rdsea image
        image: "local-management-service",
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
    let adaptorConfigBase = require('../configTempates/adaptor.json');
    adaptorConfigBase.URI = config.broker_uri;
    adaptorConfigBase.EXCHANGE =  localConfig["adaptor.amqp.input"];

    let noderedAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    noderedAdaptorConfig.ADAPTOR_NAME = `nodered_adapter_${localConfig["hinc.local.id"]}`;
    noderedAdaptorConfig.ENDPOINT = `http://noderedadapter${localConfig["hinc.local.id"]}:3004`;

    /*sensorAdaptorConfig.ENDPOINT = `http://sensorprovider${localConfig["hinc.local.id"]}:3001`;
    mqttAdaptorConfig.ENDPOINT = `http://mqttprovider${localConfig["hinc.local.id"]}:3002`
    bigqueryAdaptorConfig.ENDPOINT = `http://bigqueryprovider${localConfig["hinc.local.id"]}:3000`
    ingestionAdaptorConfig.ENDPOINT = `http://ingestionprovider${localConfig["hinc.local.id"]}:3003`
    amqpAdaptorConfig.ENDPOINT = `https://customer.cloudamqp.com/api/instances`;
    firewallAdaptorConfig.ENDPOINT = `http://firewallprovider${localConfig["hinc.local.id"]}:3008`*/


    writeAdaptorConfig(noderedAdaptorConfig);

    let noderedProviderService = {
        //TODO use rdsea image
        image: "nodered-provider"
    }

    /*let sensorProviderService = {
        image: "rdsea/sensor-provider",
    }*/

    let noderedAdaptorService = {
        //TODO use rdsea image
        image: "nodered-adaptor",
        volumes: [
            `./config/${noderedAdaptorConfig.ADAPTOR_NAME}.js:/nodered/config.js`
        ],
    }

    /*let sensorAdaptorServce = {
        image: "rdsea/sensor-adaptor",
        volumes: [
            `./config/${sensorAdaptorConfig.ADAPTOR_NAME}.js:/sensor/config.js`
        ],
    }*/

    dockerComposeHinc.services[`noderedprovider${localConfig["hinc.local.id"]}`] = noderedProviderService;
    // the amqp provider is in the cloud

    dockerComposeHinc.services[noderedAdaptorConfig.ADAPTOR_NAME] = noderedAdaptorService;

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
    let template = require("../configTempates/interoperability.json");
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
        //TODO use rdsea image
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
        //TODO use rdsea image
        image: "software-artefact-service",
        ports:[
            "8082:8082"
        ],
        depends_on:[]
    };

    dockerComposeHinc.services['software-artefact-service'] = service;
}

function clearResult(){
    if(fs.existsSync('result')){
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

        deleteFolderRecursive('result');
    }

    fs.mkdirSync('result');
    fs.mkdirSync('result/config');
}
