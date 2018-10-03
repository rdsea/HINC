const fs = require('fs');
const path = require("path");
const properties = require('properties');


module.exports = {
    create:createInteroperabilityService
};

function createInteroperabilityService(config, compositionConfig){
    let template = require("../configTemplates/interoperability.json");
    writeConfigFile(template, config);

    let service = {
        image: "rdsea/rsihubintop",
        ports:[
            `8081:${template.interoperability_service.SERVER_PORT}`
        ]
    };


    if(config.intop_mongo_db === ""){
        service.depends_on = ["mongo"];
    }

    addServiceConfigDocker(config, compositionConfig, service);

    compositionConfig.docker.services['rsihubintop'] = service;
}


function writeConfigFile(template, config){
    if(config.intop_mongo_db !== ""){
        template["interoperability_service.MONGODB_URL"] = config.intop_mongo_db;
    }

    fs.writeFileSync(path.join(__dirname, "../../result/config/interoperability_config.json"), JSON.stringify(template,null,2));
}


function addServiceConfigDocker(config, compositionConfig, service){
    let config_name = `intop_config`;
    let config_filepath = "./config/interoperability_config.json";
    let config_target = '/rsihubintop/config/production.json';

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