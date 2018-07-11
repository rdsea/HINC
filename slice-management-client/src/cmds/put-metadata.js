const fs = require('fs');
const path = require('path');
const request = require('request');
const config = require('../config');

exports.command = 'put-metadata <metadataFile> <resourceUuid> [options]'
exports.desc = 'add metadata from the json file <metadataFile> to the resource with id <resourceUuid>'

exports.builder = {
    globalURI:{
        alias: 'g',
        describe: 'use this URI for the global management service',
        type: 'string',
        demandOption: false
    }
}

exports.handler = function (argv) {
    let globalmanagementUri = config.uri;

    if(argv.globalURI){
        globalmanagementUri = argv.globalURI;
    }

    if(!hasGlobalManagementHttp(globalmanagementUri)){
        console.err("uri of global management service not set. check config");
        return;
    }

    argv.metadataFile = path.resolve(argv.metadataFile);

    fs.readFile(argv.metadataFile, 'utf8', function (err, data) {
        if (err) throw err;
        try{
            let jsonObj = JSON.parse(data);
            let requestBody = { json: true, body: jsonObj };
            let requestUri = globalmanagementUri + "/resources/" + argv.resourceUuid + "/metadata";

            request.put(requestUri, requestBody , (err, res, body) => {
                if (err) {
                    console.err("Error connecting to the global management service. Please check the http uri of the global management service " +
                        "(" + globalmanagementUri + "). Detailed error message:")
                    return console.err(err);
                }

                if(res.statusCode === 200 && body) {
                    console.log("Metadata for resource " + argv.resourceUuid + " successfully updated");
                }else if (res.statusCode === 404 && body){
                    console.err(body);
                }else{
                    console.err("Server returned unknown status: " + res.statusCode);
                }

            });
        }catch (e) {
            return console.err("The metadata file is not a correct json file");
        }
    });

};


function hasGlobalManagementHttp (globalmanagementUri){
    return globalmanagementUri !== undefined && globalmanagementUri !== null && globalmanagementUri.length>0;
}