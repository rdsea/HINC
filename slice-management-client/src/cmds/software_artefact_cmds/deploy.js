const path = require('path');
const config = require('../../config');
const reqprom = require('request-promise');
const deploy_service = require("../../services/deploy_artefact_service");

exports.command = 'deploy <software-artefact-id> <resource-provider-id>'
exports.desc = 'deploys the software artefact with id <software-artefact-id> to a new resource from the ' +
    'provider with id <resource-provider-id>';
exports.builder = {}

let softwareArtefactURL = config.software_artefact_service_uri + "/softwareartefacts";
let resourceProviderURL = config.uri + "/";

exports.handler = function (argv) {

    //TODO get softwareArtefact & resourceProvider

    let promises = [];


    let softwareArtefact = argv.softwareArtefactId;
    let resourceProvider = argv.resourceProviderId;



    promises.push(_getSoftwareArtefact(argv.softwareArtefactId)
        .then(data => {
            softwareArtefact = data;
        }));


    promises.push(_getResourceProvider(argv.resourceProviderId)
        .then(data => {
            resourceProvider = data;
        }));


    Promise.all(promises).then(()=>{

        let artefact = {artefactReference: "https://storage.googleapis.com/mytestbucket_test/csv_to_json_flows.json"};
        let provider = {uuid:"nodered"};

        deploy_service.deploy_artefact(softwareArtefact, resourceProvider)
    })
};

function _getSoftwareArtefact(softwareArtefactId){
    return new Promise((resolve,reject)=>{resolve("test")})
    //return reqprom.get(softwareArtefactURL+"/"+softwareArtefactId);
}


function _getResourceProvider(resourceProviderId){
    //TODO mocked --> implement real query
    //return reqprom.get(softwareArtefactURL+"/"+softwareArtefactId);
    return new Promise((resolve,reject)=>{resolve("test")})
}

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
