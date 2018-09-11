const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');
const request = require('request');
const config = require('../../config');

exports.command = 'list'
exports.desc = 'list software artefacts'
exports.builder = {
    limit:{
        alias: 'l',
        describe: "limit results",
        type: "number",
        demandOption: false
    },
    detailed:{
        alias: 'd',
        describe: 'display detailed JSON artefact specification',
        type: 'boolean',
        demandOption: false
    }
}

exports.handler = function (argv) {
    let requestUri = config.software_artefact_service_uri + "/softwareartefacts";
    if(argv.limit) {
        requestUri += "?limit=" + argv.limit;
    }

    request.get(requestUri, (err, res, body) => {
        if (err) {
            return console.err(err);
        }
        if(argv.detailed){
            console.log(JSON.stringify(JSON.parse(body), null, 2));
        }else{
            _displayOverview(body);
        }


    });
}

function _displayOverview(body){
    let artefacts = JSON.parse(body);
    let table = new Table({
        head: ['artefact uuid', 'execution environment', 'artefact name', "artefact reference"],
    });

    artefacts.forEach(artefact => {
        table.push([artefact.uuid || "", artefact.executionEnvironment || "", artefact.name || "", artefact.artefactReference || ""]);
    });



    console.log(table.toString());
}