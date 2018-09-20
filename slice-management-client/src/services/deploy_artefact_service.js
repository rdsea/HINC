const reqprom = require("request-promise");
const config = require("../config");

exports.deploy_artefact =  function (software_artefact, resource_provider){
    console.log("artefact:" + software_artefact);
    console.log("provider:" + resource_provider);


    _deploy_node_red(software_artefact,resource_provider);
};

function _deploy_node_red(software_artefact, resource_provider){
    let resource = {};
    let nodered_flow = {};

    _provisionNodeRedResource(resource_provider)
        .then((data) => {
            return _getNodeRedResource(resource_provider);
        })
        .then((data) => {
            resource = data[0];
            return _getNodeRedFlow(software_artefact);
        })
        .then((data) => {
            nodered_flow = data;
            return _deployFlow(resource, nodered_flow);
        })
        .then((data) => {
            console.log("node red deployed");
        })
}


function _provisionNodeRedResource(resource_provider){
    let provision_uri = `${config.uri}/controls/configure`;
    let body = {
        "adaptorName": "nodered",
        "providerUuid": resource_provider.uuid
    };

    let options = {
        method: 'POST',
        uri: provision_uri,
        body: body,
        json: true,
        timeout: 2000
    };

    //TODO remove hack: POST globalservice/controls/configure doesn't return a response, therefore let it timeout and continue
    return reqprom(options).catch(()=>{return new Promise((resolve, reject)=>{resolve("test")})});
}

function _getNodeRedResource(resource_provider){
    let query = {"providerUuid":resource_provider.uuid};
    let search_uri = `${config.uri}/resources/search`;

    let options = {
        method: 'POST',
        uri: search_uri,
        body: query,
        json: true // Automatically stringifies the body to JSON
    };
    return reqprom(options);
}

function _getNodeRedFlow(software_artefact){
    let options = {
        method: 'GET',
        uri: software_artefact.artefactReference,
        json:true
    };
    return reqprom(options);
}

function _deployFlow(resource, nodered_flow){
    let nodered_resource_uri = `${resource.parameters.ingressAccessPoints[0].host}/flows`;
    let options = {
        method: 'POST',
        uri: nodered_resource_uri,
        body: nodered_flow,
        json: true // Automatically stringifies the body to JSON
    };
    return reqprom(options);
}