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

    let noderedAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    noderedAdaptorConfig.ADAPTOR_NAME = `nodered_adapter_${localConfig["hinc.local.id"]}`;
    noderedAdaptorConfig.ENDPOINT = `http://noderedadapter${localConfig["hinc.local.id"]}:3004`;

    writeAdaptorConfig(noderedAdaptorConfig);
    let service = {
        image: "rdsea/nodered-adaptor"
    };

    let config_name = `nodered_adapter_config_${localConfig["hinc.local.id"]}`;
    let config_filepath = `./config/nodered_adapter_${localConfig["hinc.local.id"]}.json`;
    let config_target = "/noderedadaptor/config/production.json";

    addServiceConfigDocker(compositionConfig, config.is_docker_stack, service, config_name, config_filepath, config_target);

    compositionConfig.docker.services[noderedAdaptorConfig.ADAPTOR_NAME] = service;
}

function createProvider(config, compositionConfig, localConfig){
    let template = require("../../configTemplates/providers/nodered-datatransformer-provider");

    writeProviderConfig(template, config);

    let service = {
        image: "rdsea/nodered-provider"
    };

    let config_name = `nodered_provider_config`;
    let config_filepath = `./config/noderedprovider.json`;
    let config_target = "/noderedprovider/config/production.json";

    addServiceConfigDocker(compositionConfig, config.is_docker_stack, service, config_name, config_filepath, config_target);

    compositionConfig.docker.services[`nodered-provider-${localConfig["hinc.local.id"]}`] = service;
}


function writeAdaptorConfig(adaptorConfig){
    let config = `{
      "noderedadaptor": {
        "ADAPTOR_NAME" : "${adaptorConfig.ADAPTOR_NAME}",
            "URI" : "${adaptorConfig.URI}",
            "EXCHANGE" : "${adaptorConfig.EXCHANGE}",
            "ENDPOINT" : "${adaptorConfig.ENDPOINT}"
      }
    }`;
    fs.writeFileSync(path.join(__dirname, `../../../result/config/${adaptorConfig.ADAPTOR_NAME}.json`), config);
}


function writeProviderConfig(template, config){
    let provider_config = `{
  "kubeoptions": {
    "minikube_nodeport": ${template.kubeoptions.minikube_nodeport},
    "max_instances": ${template.kubeoptions.max_instances}
  }
}`;
    fs.writeFileSync(path.join(__dirname, `../../../result/config/noderedprovider.json`), provider_config);
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
