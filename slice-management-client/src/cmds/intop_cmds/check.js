const prompt = require('inquirer').createPromptModule();
const request = require('request');
const config = require("../../config")
const path = require('path');

exports.command = 'check <file>'
exports.desc = 'perform an interoperability check on the a slice specified in <file>'
exports.builder = {}

exports.handler = function (argv) {
    let filepath = path.resolve(argv.file);
    let s;
    try {
        s = require(filepath);
    }catch (e) {
        throw new Error(`could not find slice for file ${argv.file}, please try again`);
    }

    let jsonObj = {slice:s}
    let requestBody = { json: true, body: jsonObj };
    let requestUri = config.intop_service_uri + "/interoperability/check";

    request.post(requestUri, requestBody , (err, res, body) => {
        if (err) {
            console.err("Error connecting to the interoperability service. Please check the http uri of the interoperability service" +
                "(" + config.intop_service_uri + "). Detailed error message:")
            return console.err(err);
        }

        printCheckResults(body);


    });
};

function printCheckResults(body){

    console.info("Interoperability check results: \n");
    if(body.errors.length>0) {
        console.info("Errors:");
        for (let i = 0; i < body.errors.length; i++) {
            console.info((i+1) + ": \tError: \n\t\t\t"+ body.errors[i].log.nodes);
            console.info("\t\tMetadata: \n\t\t\t"+ JSON.stringify(body.errors[i].log.metadata));
            console.info("\t\tPath: \n\t\t\t"+ body.errors[i].log.path);
            console.info("");


        }
        console.info("");
    }

    if(body.warnings.length>0){
        console.info("Warnings:");
        for(let i = 0; i<body.warnings.length; i++) {
            console.info(body.warnings[i].log);
        }
        console.info("");
    }

    if(body.matches.length>0){
        console.info("Matches:");
        for(let i = 0; i<body.matches.length; i++){
            console.info("*\t" + body.matches[i].source.resource.name + " -> " +  body.matches[i].target.resource.name);
        }
    }
}