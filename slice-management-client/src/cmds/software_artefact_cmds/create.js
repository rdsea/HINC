const path = require('path');
const config = require('../../config');
const request = require('request');
const storage_service = require("../../services/storage_service");

exports.command = 'create <software-artefact> <execution-environment> <metadata-file> <name>'
exports.desc = 'creates a software artefact specified in <software-artefact> for the execution environment <execution-environment> ' +
    ' with metadata specified in <metadata-file> and the name <name>'
exports.builder = {}

let requestUri = config.software_artefact_service_uri + "/softwareartefacts";

exports.handler = function (argv) {
    requestUri = config.software_artefact_service_uri + "/softwareartefacts";


    let filePath_metadata= path.resolve(argv.metadataFile);
    let metadataFile = require(filePath_metadata);

    _getSoftwareArtefactURL(argv.softwareArtefact)
        .then((softwareArtefactURL) => {
            let jsonObj =
                {
                    artefactReference: softwareArtefactURL,
                    executionEnvironment: argv.executionEnvironment,
                    metadata: metadataFile,
                    name: argv.name
                };
            _putSoftwareArtefact(jsonObj);
        });

};

function _getSoftwareArtefactURL(softwareArtefact){
    if(!softwareArtefact.startsWith("http")){
        return storage_service.upload(softwareArtefact);
    }else{
        return new Promise((resolve,reject) => {resolve(softwareArtefact)});
    }
}


function _putSoftwareArtefact(requestBody){
    let body = { json: true, body: requestBody };

    request.put(requestUri, body, (err, res, body) => {
        if (err) {
            console.err("Error connecting to the software artefact service. Please check the http uri of the software artefact service" +
                "(" + config.software_artefact_service_uri + "). Detailed error message:")
            return console.err(err);
        }
        console.log(body);
    });
}
