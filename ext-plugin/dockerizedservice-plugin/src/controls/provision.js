const axios = require('axios');
const configModule = require('config');
const config = configModule.get('dockerizedservice');
const qs = require("qs");
const parse = require("url-parse");
const assert = require('assert');

function provision(resource){
    let controlResult = null;
    let dockerizedservice = null;
    //just hard code for testing
    let provisionParameters = resource.parameters;


    return axios.post(`${config.ENDPOINT}`, provisionParameters).catch((err) => {
                console.error(err)
                throw err;
    }).then((dockerizedservice) => {
      //tobe updated
        let dockerizedserviceUrl = parse(config.ENDPOINT);

        let ingressAccessPoint = {
            applicationProtocol: "HTTP",
            host: dockerizedserviceUrl.hostname,
            port: dockerizedservice.ports,
            accessPattern: "DIRECT",
            networkProtocol: "IP"
        };

        resource.parameters.ingressAccessPoints[0] = ingressAccessPoint;
        resource.uuid = dockerizedservice.serviceId;

        let controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(resource),
            resourceUuid: resource.uuid,
        };
        console.log(controlResult);
        return controlResult

    }).catch((err) => {
        console.log('control execution failed');
        console.error(err.response)
        controlResult = {
            status: 'FAILED',
            rawOutput: err.response,
        };
        return controlResult;
    });
}

module.exports.provision = provision;
