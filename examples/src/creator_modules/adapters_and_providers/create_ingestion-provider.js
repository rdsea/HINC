const fs = require('fs');
const path = require("path");
const properties = require('properties');


module.exports = {
    create:createNodeRedAdaptorAndProvider
}

function createNodeRedAdaptorAndProvider(config, compositionConfig, localConfig){
    createAdapter(config,compositionConfig,localConfig);
    createProvider(config,compositionConfig,localConfig);
}

function createAdapter(config, compositionConfig, localConfig){
    let adaptorConfigBase = require('../../configTemplates/adaptor.json');
    adaptorConfigBase.URI = config.broker_uri;
    adaptorConfigBase.EXCHANGE =  localConfig["adaptor.amqp.input"];

    let ingestionAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    ingestionAdaptorConfig.ADAPTOR_NAME = `ingestion${localConfig["hinc.local.id"]}`;
    ingestionAdaptorConfig.ENDPOINT = `http://ingestionprovider${localConfig["hinc.local.id"]}:3003`

    writeAdaptorConfig(ingestionAdaptorConfig);
    let service = {
        image: "rdsea/ingestion-adaptor"
    };

    let config_name = `${ingestionAdaptorConfig.ADAPTOR_NAME}_config`;
    let config_filepath = `./config/${ingestionAdaptorConfig.ADAPTOR_NAME}.json`;
    let config_target = "/ingestionadaptor/config/production.json";

    addServiceConfigDocker(compositionConfig, config.is_docker_stack, service, config_name, config_filepath, config_target);

    compositionConfig.docker.services[ingestionAdaptorConfig.ADAPTOR_NAME] = service;
}

function createProvider(config, compositionConfig, localConfig){
    let service = {
        image: "rdsea/ingestion-provider"
    };

    compositionConfig.docker.services[`ingestion-provider-${localConfig["hinc.local.id"]}`] = service;
}


function writeAdaptorConfig(adaptorConfig){
    let config = `{
      "ingestionadaptor": {
        "ADAPTOR_NAME" : "${adaptorConfig.ADAPTOR_NAME}",
            "URI" : "${adaptorConfig.URI}",
            "EXCHANGE" : "${adaptorConfig.EXCHANGE}",
            "ENDPOINT" : "${adaptorConfig.ENDPOINT}"
      }
    }`;
    fs.writeFileSync(path.join(__dirname, `../../../result/config/${adaptorConfig.ADAPTOR_NAME}.json`), config);
}


function writeProviderConfig(template, config){
}



function addServiceConfigDocker(compositionConfig, isDockerStack, service, config_name, config_filepath, config_target){
    if(isDockerStack){
        service.configs = [{source: config_name,
            target: config_target}];

        compositionConfig.docker.configs[config_name] = {
            file: config_filepath
        };
    }else{
        service.volumes = [
            `${config_filepath}:${config_target}`
        ]
    }
}
