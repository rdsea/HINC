const fs = require('fs');
const path = require("path");
const properties = require('properties');


module.exports = {
    create:createInteroperabilityService
};

function createInteroperabilityService(config, compositionConfig){
    let template = require("../configTempates/interoperability.json");
    template['GLOBAL_MANAGEMENT_URI'] = "http://global:8080";
    template['SOFTWARE_ARTEFACT_URI'] = "http://software-artefact-service:8082";

    let interoperabilityConfig = `{
        "interoperability_service": {
            "SOFTWARE_ARTEFACT_URI": "${template.interoperability_service.SOFTWARE_ARTEFACT_URI}",
            "GLOBAL_MANAGEMENT_URI": "${template.interoperability_service.GLOBAL_MANAGEMENT_URI}",
            "SEARCH_ARTEFACTS": "${template.interoperability_service.SEARCH_ARTEFACTS}",
            "SEARCH_RESOURCES": "${template.interoperability_service.SEARCH_RESOURCES}",
            "SEARCH_BRIDGE": "${template.interoperability_service.SEARCH_BRIDGE}",
            "SERVER_PORT": "${template.interoperability_service.SERVER_PORT}",
            "MONGODB_URL": "${template.interoperability_service.MONGODB_URL}",
            "BRIDGE_COLLECTION_NAME": "${template.interoperability_service.BRIDGE_COLLECTION_NAME}"
        }
    }`;

    fs.writeFileSync(path.join(__dirname, "../../result/config/interoperability_config.json"), interoperabilityConfig);

    let service = {
        image: "rdsea/rsihubintop",
        ports:[
            `8081:${template.interoperability_service.SERVER_PORT}`
        ],
        depends_on:[],
        configs:[{source: 'intop_config',
            target: '/rsihubintop/config/production.json'}]
    };

    let service_config = {
        file: "./config/interoperability_config.json"
    };

    compositionConfig.docker.configs[`intop_config`] = service_config;
    compositionConfig.docker.services['intop'] = service;
}