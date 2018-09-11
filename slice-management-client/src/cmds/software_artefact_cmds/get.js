const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');
const request = require('request');
const config = require('../../config');

exports.command = 'get <artefactId>'
exports.desc = 'get details about software artefact with id <artefactId>'
/*exports.builder = {
    detailed:{
        alias: 'd',
        describe: 'display detailed JSON artefact specification',
        type: 'boolean',
        demandOption: false
    }
}*/

exports.handler = function (argv) {
    let requestUri = config.software_artefact_service_uri + "/softwareartefacts";
    requestUri += "/" + argv.artefactId;

    request.get(requestUri, (err, res, body) => {
        if (err) {
            /*console.err("Error connecting to the interoperability service. Please check the http uri of the interoperability service" +
                "(" + config.intop_service_uri + "). Detailed error message:")*/
            return console.err(err);
        }
        //if(argv.detailed){
            console.log(JSON.stringify(JSON.parse(body), null, 2));
        /*}else{
            _displayOverview(body);
        }*/


    });
}

function _displayOverview(artefact){
    artefact = JSON.parse(artefact);
    let table = new Table({
        head: ['artefact uuid', 'execution environment', 'artefact name', "artefact reference"],
    });

    table.push([artefact.uuid || "", artefact.executionEnvironment|| "", artefact.name || "", artefact.artefactReference || ""]);


    console.log(table.toString());
}