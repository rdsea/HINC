const axios = require('axios');
const config = require('../../config');
const randomString = require('randomstring');

function provision(resource){
    let controlResult = null;

    let provisionParameters = {
        alarmBroker:{
            host: resource.parameters.egressAccessPoints[0].host,
            port: resource.parameters.egressAccessPoints[0].port,
            topics: resource.parameters.egressAccessPoints[0].topics,
        },

        vesselBroker: {
            host: resource.parameters.egressAccessPoints[1].host,
            port: resource.parameters.egressAccessPoints[1].port,
            topics: resource.parameters.egressAccessPoints[1].topics,
        }
    }

    console.log(`making http call with config: `);
    console.log(JSON.stringify(provisionParameters, null, 2));

    return axios.post(`${config.ENDPOINT}/alarmclient`, provisionParameters).then((res) => {
        let alarmClient = res.data;
        resource.uuid = alarmClient.alarmclientId;
        controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(resource),
            resourceUuid: alarmClient.alarmclientId,
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

module.exports = provision;