const fs = require('fs');
const path = require("path");
const properties = require('properties');


module.exports = {create: createGlobal};

function createGlobal(config, compositionConfig){
    let globalConfig = require("../configTemplates/global.json");
    globalConfig['spring.rabbitmq.addresses'] = config.broker_uri

    fs.writeFileSync(path.join(__dirname, "../../result/config/global.properties"), properties.stringify(globalConfig));

    let service = {
        image: "rdsea/rsihubglobal",
        ports:[
            "8080:8080"
        ],
        depends_on:[]
    };

    addServiceConfigDocker(config, compositionConfig, service);

    compositionConfig.docker.services['rsihubglobal'] = service;
    compositionConfig.exchanges[globalConfig["hinc.global.rabbitmq.input"]] = {
        type: 'fanout',
        queues:[
            globalConfig["hinc.global.rabbitmq.input"]
        ],
    }
}

function addServiceConfigDocker(config, compositionConfig, service){
    let config_name = `global_config`;
    let config_filepath = "./config/global.properties";
    let config_target = '/rsihubglobal/application.properties';

    if(config.is_docker_stack){
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