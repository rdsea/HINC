const fs = require('fs');
const path = require("path");
const properties = require('properties');

module.exports = {create: createSoftwareArtefactService};

function createSoftwareArtefactService(config, compositionConfig){

    let service = {
        image: "rdsea/rsihubsas",
        ports:[
            "8082:8082"
        ],
        depends_on:[]
    };

    compositionConfig.docker.services['sas'] = service;
}