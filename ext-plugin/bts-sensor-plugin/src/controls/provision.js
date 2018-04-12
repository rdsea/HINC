const axios = require('axios');

function sendControl(controlPoint){ 
    let controlResult = null;
    let config = {
        method: controlPoint.accessPoints[0].httpMethod,
        url: controlPoint.accessPoints[0].uri,
        data: controlPoint.parameters,
    }

    console.log(`making http call with config: `);
    console.log(JSON.stringify(config, null, '\t'));
    return axios(config).then((res) => {
        let sensor = res.data;
        controlResult = {
            status: 'SUCCESS',
            rawOutput: sensor,
            resourceUuid: sensor.clientId,
        };
        console.log('successfuly control execution');
        console.log(JSON.stringify(controlResult, null, '\t'));
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

module.exports.sendControl = sendControl;