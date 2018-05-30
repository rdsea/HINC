const prompt = require('inquirer').createPromptModule();
const questions = require('./src/questions');
const properties = require('properties');
const fs = require('fs');
const yml = require('js-yaml');
const setupBroker = require('./src/amqpTools');


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
    fs.writeFileSync("result/docker-compose.yml", yml.safeDump(dockerComposeHinc));
    
});

function create(config){
    createGlobal(config);
    createAllLocals(config);
    createAllAdaptorsAndProviders(config)

    console.log("navigate to the folder ./result and run the command:")
    console.log("$ docker-compose up");
}

function createGlobal(config){
    let globalConfig = require("./src/configTempates/global.json");
    globalConfig['spring.rabbitmq.addresses'] = config.broker_uri

    fs.writeFileSync("result/config/global.properties", properties.stringify(globalConfig));

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
    let localConfig = require("./src/configTempates/local.json");
    localConfig["spring.rabbitmq.addresses"] = config.broker_uri;
    localConfig["hinc.local.id"] = localId;
    localConfig["adaptor.amqp.input"] = `adaptor_${localId}_input`;
    localConfig["adaptor.amqp.output.broadcast"] = `adaptor_${localId}_broadcast`;
    localConfig["adaptor.amqp.output.unicast"] = `adaptor_${localId}_unicast`
    fs.writeFileSync(`result/config/local.${localId}.properties`, properties.stringify(localConfig));

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
    let adaptorConfigBase = require('./src/configTempates/adaptor.json');
    adaptorConfigBase.URI = config.broker_uri;
    adaptorConfigBase.EXCHANGE =  localConfig["adaptor.amqp.input"];
    
    let sensorAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let mqttAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let bigqueryAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let ingestionAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));

    sensorAdaptorConfig.ADAPTOR_NAME = `sensor${localConfig["hinc.local.id"]}`;
    mqttAdaptorConfig.ADAPTOR_NAME = `mqtt${localConfig["hinc.local.id"]}`;
    bigqueryAdaptorConfig.ADAPTOR_NAME = `bigquery${localConfig["hinc.local.id"]}`;
    ingestionAdaptorConfig.ADAPTOR_NAME = `ingestion${localConfig["hinc.local.id"]}`;

    sensorAdaptorConfig.ENDPOINT = `http://sensorprovider${localConfig["hinc.local.id"]}:3001`;
    mqttAdaptorConfig.ENDPOINT = `http://mqttprovider${localConfig["hinc.local.id"]}:3002`
    bigqueryAdaptorConfig.ENDPOINT = `http://bigqueryprovider${localConfig["hinc.local.id"]}:3000`
    ingestionAdaptorConfig.ENDPOINT = `http://ingestionprovider${localConfig["hinc.local.id"]}:3003`

    writeAdaptorConfig(sensorAdaptorConfig);
    writeAdaptorConfig(mqttAdaptorConfig);
    writeAdaptorConfig(bigqueryAdaptorConfig);
    writeAdaptorConfig(ingestionAdaptorConfig);

    let sensorProviderService = {
        image: "rdsea/sensor-provider",
    }

    let mqttProviderService = {
        image: "rdsea/mqtt-provider",
    }

    let bigqueryProviderService = {
        image: "rdsea/bigquery-provider",
    }

    let ingestionProviderService = {
        image: "rdsea/ingestion-provider",
    }

    let sensorAdaptorServce = {
        image: "rdsea/sensor-adaptor",
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

    let bigqueryAdaptorServce = {
        image: "rdsea/bigquery-adaptor",
        volumes: [
            `./config/${bigqueryAdaptorConfig.ADAPTOR_NAME}.js:/bigquery/config.js`
        ],
    }

    let ingestionAdaptorServce = {
        image: "rdsea/ingestion-adaptor",
        volumes: [
            `./config/${ingestionAdaptorConfig.ADAPTOR_NAME}.js:/ingestion/config.js`
        ],
    }

    dockerComposeHinc.services[`sensorprovider${localConfig["hinc.local.id"]}`] = sensorProviderService;
    dockerComposeHinc.services[`mqttprovider${localConfig["hinc.local.id"]}`] = mqttProviderService;
    dockerComposeHinc.services[`bigqueryprovider${localConfig["hinc.local.id"]}`] = bigqueryProviderService;
    dockerComposeHinc.services[`ingestionprovider${localConfig["hinc.local.id"]}`] = ingestionProviderService;

    dockerComposeHinc.services[sensorAdaptorConfig.ADAPTOR_NAME] = sensorAdaptorServce;
    dockerComposeHinc.services[mqttAdaptorConfig.ADAPTOR_NAME] = mqttAdaptorServce;
    dockerComposeHinc.services[bigqueryAdaptorConfig.ADAPTOR_NAME] = bigqueryAdaptorServce;
    dockerComposeHinc.services[ingestionAdaptorConfig.ADAPTOR_NAME] = ingestionAdaptorServce;

}

function writeAdaptorConfig(adaptorConfig){
    let config = `module.exports = {
        ADAPTOR_NAME : '${adaptorConfig.ADAPTOR_NAME}',
        URI : '${adaptorConfig.URI}',
        EXCHANGE : '${adaptorConfig.EXCHANGE}',
        ENDPOINT: '${adaptorConfig.ENDPOINT}'
    }`

    fs.writeFileSync(`result/config/${adaptorConfig.ADAPTOR_NAME}.js`, config);
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
