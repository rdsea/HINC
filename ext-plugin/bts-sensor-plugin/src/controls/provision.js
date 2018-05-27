const axios = require('axios');
const config = require('../../config')

function provision(resource){
    let data = {
        uri: `tcp://${resource.metadata._proxy.ip}:${resource.metadata._proxy.port}`,
        topic: resource.metadata.parameters.topic,
    }

    console.log(`making http call with config: `);
    console.log(JSON.stringify(data, null, '\t'));
    return axios.post(`${config.ENDPOINT}/sensor/bts/humidity`, data).then((res) => {
        let sensor = res.data;
        resource.uuid = sensor.clientId;
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