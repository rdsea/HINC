const axios = require('axios');
const config = require('../../config')

function provision(resource){

    let data = {
        uri: `tcp://${resource.parameters.egressAccessPoints[0].host}:${resource.parameters.egressAccessPoints[0].port}`,
        topic: resource.parameters.egressAccessPoints[0].topics[0],
    }

    console.log(`making http call with config: `);
    console.log(JSON.stringify(data, null, '\t'));
    return axios.post(`${config.ENDPOINT}/sensor/bts/humidity`, data).then((res) => {
        let sensor = res.data;
        resource.uuid = sensor.clientId;
        resource.parameters.egressAccessPoints[0].host = sensor.uri.match(/\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b/)[0];
        resource.parameters.egressAccessPoints[0].port = parseInt(sensor.uri.match(/(?<=:)\d{1,4}/)[0])

        controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(resource),
            resourceUuid: sensor.clientId,
        };
        console.log('successfuly control execution');
        console.log(JSON.stringify(controlResult, null, '\t'));
        return controlResult;
    }).catch((err) => {
        console.log('control execution failed');
        console.error(err)
        controlResult = {
            status: 'FAILED',
            rawOutput: err,
        };
        return controlResult;
    });
}

module.exports.provision = provision;