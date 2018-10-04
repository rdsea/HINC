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

    let alarmclientAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    alarmclientAdaptorConfig.ADAPTOR_NAME = `alarmclient${localConfig["hinc.local.id"]}`;
    alarmclientAdaptorConfig.ENDPOINT = `http://alarmclientprovider${localConfig["hinc.local.id"]}:3006`;

    writeAdaptorConfig(alarmclientAdaptorConfig);
    let service = {
        image: "rdsea/alarmclient-adaptor"
    };

    let config_name = `${alarmclientAdaptorConfig.ADAPTOR_NAME}_config`;
    let config_filepath = `./config/${alarmclientAdaptorConfig.ADAPTOR_NAME}.js`;
    let config_target = "/seaportalarm-plugin/config.js";

    addServiceConfigDocker(compositionConfig, config.is_docker_stack, service, config_name, config_filepath, config_target);

    compositionConfig.docker.services[alarmclientAdaptorConfig.ADAPTOR_NAME] = service;
}

function createProvider(config, compositionConfig, localConfig){
    let service = {
        image: "rdsea/alarmclient-provider"
    };

    compositionConfig.docker.services[`alarmclientprovider${localConfig["hinc.local.id"]}`] = service;
}


function writeAdaptorConfig(adaptorConfig){
    let config = `module.exports = {
        ADAPTOR_NAME : '${adaptorConfig.ADAPTOR_NAME}',
        URI : '${adaptorConfig.URI}',
        EXCHANGE : '${adaptorConfig.EXCHANGE}',
        ENDPOINT: '${adaptorConfig.ENDPOINT}'
    }`;
    fs.writeFileSync(path.join(__dirname, `../../../result/config/${adaptorConfig.ADAPTOR_NAME}.js`), config);
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
