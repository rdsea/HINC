const path = require('path');
const config = require('../../config');
const reqprom = require('request-promise');
const deploy_service = require("../../services/deploy_artefact_service");

exports.command = 'deploy <software-artefact-id> <resource-provider-id>'
exports.desc = 'deploys the software artefact with id <software-artefact-id> to a new resource from the ' +
    'provider with id <resource-provider-id>';
exports.builder = {
    useResourceID:{
        //alias: 'u',
        describe: 'deploy the artefact to the resource with provided resourceID',
        type: 'String',
        demandOption: false
    }
}

let softwareArtefactURL = config.software_artefact_service_uri + "/softwareartefacts";
let resourceProviderURL = config.uri + "/resourceproviders";

exports.handler = function (argv) {


    let promises = [];


    let softwareArtefact;
    let resourceProvider;



    promises.push(_getSoftwareArtefact(argv.softwareArtefactId)
        .then(data => {
            softwareArtefact = data;
        }));


    if(argv.useResourceID === undefined) {
        promises.push(_getResourceProvider(argv.resourceProviderId)
            .then(data => {
                resourceProvider = data;
            }));
    }


    Promise.all(promises).then(()=>{

        /*softwareArtefact = {artefactReference: "https://raw.githubusercontent.com/rdsea/IoTCloudSamples/master/IoTCloudUnits/node_red_dataflows/csv_to_json_flow/flow_csv_to_json.json"};
        resourceProvider = {uuid:"nodered"};*/

        if(argv.useResourceID){
            deploy_service.deploy_to_resource(softwareArtefact, argv.useResourceID)
        }else{
            deploy_service.deploy_artefact(softwareArtefact, resourceProvider)
        }
    })
};

function _getSoftwareArtefact(softwareArtefactId){
    let options = {
        method: 'GET',
        uri: softwareArtefactURL+"/"+softwareArtefactId,
        json:true
    };
    return reqprom(options);
}


function _getResourceProvider(resourceProviderId){
    let options = {
        method: 'GET',
        uri: resourceProviderURL+"/"+resourceProviderId,
        json:true
    };
    return reqprom(options);
}
