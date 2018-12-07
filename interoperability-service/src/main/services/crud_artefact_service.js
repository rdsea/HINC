const reqprom = require('request-promise');
const configModule = require('config');

const config = configModule.get('interoperability_service');

const baseUri = config.SOFTWARE_ARTEFACT_URI+"/softwareartefacts";

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