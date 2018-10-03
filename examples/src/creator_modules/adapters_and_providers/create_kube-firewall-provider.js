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

    let firewallAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    firewallAdaptorConfig.ADAPTOR_NAME = `firewall${localConfig["hinc.local.id"]}`
    firewallAdaptorConfig.ENDPOINT = `http://firewallprovider${localConfig["hinc.local.id"]}:3008`

    writeAdaptorConfig(firewallAdaptorConfig);
    let service = {
        image: "rdsea/firewall-adaptor"
    };

    let config_name = `${firewallAdaptorConfig.ADAPTOR_NAME}_config`;
    let config_filepath = `./config/${firewallAdaptorConfig.ADAPTOR_NAME}.js`;
    let config_target = "/adaptor/config.js";

    addServiceConfigDocker(compositionConfig, config.is_docker_stack, service, config_name, config_filepath, config_target);

    compositionConfig.docker.services[firewallAdaptorConfig.ADAPTOR_NAME] = service;
}

function createProvider(config, compositionConfig, localConfig){
    let service = {
        image: "rdsea/kube-firewall-provider"
    };

    compositionConfig.docker.services[`kube-firewall-provider-${localConfig["hinc.local.id"]}`] = service;
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
