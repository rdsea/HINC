const meshService = require('../../services/mesh/mesh');
const amqpTools = require('../../amqpTools');
const db = require('../../data/db');
const moment = require('moment');
const path = require('path');
const axios = require('axios');
const config = require('../../config');
const fs = require("fs");
const request = require('request');

exports.command = 'create <software-artefact-file> <execution-environment> <metadata-file> <name>'
exports.desc = 'creates a software artefact specified in <software-artefact-file> for the execution environment <execution-environment> ' +
    ' with metadata specified in <metadata-file> and the name <name>'
exports.builder = {}


exports.handler = function (argv) {
    let filePath_softwareArtefact= path.resolve(argv.softwareArtefactFile);
    //TODO change to read file
    let softwareArtefact = require(filePath_softwareArtefact);

    let filePath_metadata= path.resolve(argv.metadataFile);
    let metadataFile = require(filePath_metadata);


    let requestUri = config.software_artefact_service_uri + "/softwareartefacts";

    let jsonObj =
        {
            artefactReference: JSON.stringify(softwareArtefact),
            executionEnvironment: argv.executionEnvironment,
            metadata: metadataFile,
            name: argv.name
        };

    let requestBody = { json: true, body: jsonObj };

    request.put(requestUri, requestBody , (err, res, body) => {
        if (err) {
            console.err("Error connecting to the software artefact service. Please check the http uri of the software artefact service" +
                "(" + config.software_artefact_service_uri + "). Detailed error message:")
            return console.err(err);
        }
        console.log(body);
    });


    //first version sketch:
    //input:
    // * software-artefact-file
    // * execution-environment (simple string for now)
    // * metadata-file (later maybe change to prompt)
    // * name


    //optimal solution:
    // * set input in command with -optionflag
    // * prompt for unprovided inputs
}

