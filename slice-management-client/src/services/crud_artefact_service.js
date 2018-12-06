const reqprom = require('request-promise');
const config = require("../config");

let baseUri = config.software_artefact_service_uri + "/softwareartefacts";

module.exports = {
    createArtefact:_createArtefact,
    deleteArtefact:_deleteArtefact
}

function _createArtefact(artefact){


    let options = {
        method: 'PUT',
        uri: baseUri,
        body: artefact,
        json: true // Automatically stringifies the body to JSON
    };
    return reqprom(options);
}


function _deleteArtefact(artefact){
    let requestUri = baseUri + "/" + artefact.uuid ;

    let options = {
        method: 'DELETE',
        uri: requestUri,
        json: true // Automatically stringifies the body to JSON
    };
    return reqprom(options);
}