const axios = require('axios');
const config = require('../../config')
const url = require("url");


let urls = {
    weather: '/sensor/valencia/weather',
    sound: '/sensor/valencia/sound',
    emission: '/sensor/valencia/emission',
    alarm: '/sensor/valencia/alarm'
}

function provision(resource){

    let data = {
        uri: `tcp://${resource.parameters.egressAccessPoints[0].host}:${resource.parameters.egressAccessPoints[0].port}`,
        topic: resource.parameters.egressAccessPoints[0].topics[0],
    }

    console.log(`making http call with config: `);
    console.log(JSON.stringify(data, null, '\t'));
    return axios.post(`${config.ENDPOINT}${urls[resource.name]}`, data).then((res) => {
        let sensor = res.data;
        resource.uuid = sensor.clientId;
        let uri = url.parse(sensor.uri)
        resource.parameters.egressAccessPoints[0].host = uri.hostname;
        resource.parameters.egressAccessPoints[0].port = parseInt(uri.port)


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

