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
    /*requestUri = config.software_artefact_service_uri + "/softwareartefacts";



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
        });*/

    //TODO get softwareArtefact & resourceProvider

    let promises = [];


    let softwareArtefact = argv.softwareArtefactId;
    let resourceProvider = argv.resourceProviderId;

    promises.push(reqprom.get(softwareArtefactURL+"/"+argv.softwareArtefactId)
        .then(data => {
            softwareArtefact = data;
        }));

    promises.push()

    Promise.all(promises).then(()=>{
        deploy_service.deploy_artefact(softwareArtefact, resourceProvider)
    })
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
