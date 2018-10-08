const Table = require('cli-table');
const reqprom = require('request-promise');
const config = require('../../config');
const path = require("path");

exports.command = 'search <query>'
exports.desc = 'search interoperability software artefacts with a mongo db query document'
exports.builder = {}
exports.handler = function (argv) {

    let query = argv.query;

    if(!_isJsonString(query)) {
        try {
            query = require(path.resolve(query));
        }catch(err){
            console.err(`${query} is neither a valid JSON string nor a valid path`)
            return;
        }
    }else{
        query = JSON.parse(query);
    }

    return _searchArtefacts(query).then((data) => {
        let artefacts = data;
        let table = new Table({
            head: ['artefact ID', 'name', 'executionEnvironment', 'reference'],
        });
        artefacts.forEach((artefact) => {
            table.push([
                artefact.uuid,
                artefact.name,
                artefact.executionEnvironment,
                artefact.artefactReference
            ]);
        });

        console.log(table.toString());
    })
};


function _searchArtefacts(query){
    let options = {
        uri:`${config.software_artefact_service_uri}/softwareartefacts/search`,
        body:query,
        json:true,
        method:'POST'
    };
    return reqprom(options)
}


function _isJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}