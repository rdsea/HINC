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
        ],
        depends_on:[]
    };

    addServiceConfigDocker(config, compositionConfig, service);

    compositionConfig.docker.services['rsihubintop'] = service;
}


function writeConfigFile(template, config){
    template['GLOBAL_MANAGEMENT_URI'] = "http://rsihubglobal:8080";
    template['SOFTWARE_ARTEFACT_URI'] = "http://rsihubsas:8082";

    let interoperabilityConfig = `{
        "interoperability_service": {
            "SOFTWARE_ARTEFACT_URI": "${template.interoperability_service.SOFTWARE_ARTEFACT_URI}",
            "GLOBAL_MANAGEMENT_URI": "${template.interoperability_service.GLOBAL_MANAGEMENT_URI}",
            "SEARCH_ARTEFACTS": "${template.interoperability_service.SEARCH_ARTEFACTS}",
            "SEARCH_RESOURCES": "${template.interoperability_service.SEARCH_RESOURCES}",
            "SEARCH_BRIDGE": "${template.interoperability_service.SEARCH_BRIDGE}",
            "SERVER_PORT": "${template.interoperability_service.SERVER_PORT}",
            "MONGODB_URL": "${config.intop_mongo_db}",
            "BRIDGE_COLLECTION_NAME": "${template.interoperability_service.BRIDGE_COLLECTION_NAME}"
        }
    }`;

    fs.writeFileSync(path.join(__dirname, "../../result/config/interoperability_config.json"), interoperabilityConfig);
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