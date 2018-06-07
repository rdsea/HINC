const axios = require('axios');
const config = require('../../config');

function provision(resource){
    let controlResult = null;

    let provisionParameters = {
        vessel:{
            boat: resource.parameters.boat,
            status: resource.parameters.status,
            arrival: resource.parameters.arrival,
            departure: resource.parameters.departure,
            destination: resource.parameters.destination,
            terminal: resource.parameters.terminal,
        },

        broker:{
            host: resource.parameters.egressAccessPoints[0].host,
            port: resource.parameters.egressAccessPoints[0].port,
        }
    }

    console.log(`making http call with config: `);
    console.log(JSON.stringify(provisionParameters, null, 2));

    return axios.post(`${config.ENDPOINT}/vessel`, provisionParameters).then((res) => {
        let vessel = res.data;
        resource.uuid = vessel.boatId;
        controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(resource),
            resourceUuid: vessel.boatId,
        };
        console.log('successfuly control execution');
        console.log(JSON.stringify(controlResult, null, 2));
        return controlResult;
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
