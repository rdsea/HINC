const fs = require('fs');
const path = require("path");
const properties = require('properties');

module.exports = {create: createSoftwareArtefactService};

function createSoftwareArtefactService(config, compositionConfig){
    let template = require("../configTemplates/sas.json");
    writeConfigFile(template, config);


    let service = {
        image: "rdsea/rsihubsas",
        ports:[
            "8082:8082"
        ],
        depends_on:[]
    };

    addServiceConfigDocker(config, compositionConfig, service);

    compositionConfig.docker.services['rsihubsas'] = service;
}

function writeConfigFile(template, config){

    template['spring.data.mongodb.uri'] = config.sas_mongo_db;

    fs.writeFileSync(path.join(__dirname, "../../result/config/sas.properties"), properties.stringify(template));
}


function addServiceConfigDocker(config, compositionConfig, service){
    let config_name = `sas_config`;
    let config_filepath = "./config/sas.properties";
    let config_target = '/rsihubsas/application.properties';

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