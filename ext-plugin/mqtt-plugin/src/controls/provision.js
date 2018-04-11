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
        let broker = res.data;
        controlResult = {
            status: 'SUCCESS',
            rawOutput: broker,
            resourceUuid: broker.brokerId,
        };
        console.log('successfuly control execution');
        
        // location of broker might not set, await it
        return _waitForLocation(broker.brokerId, config.url);
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

function _waitForLocation(brokerId, uri){
    return axios.get(`${uri}/${brokerId}`).then((res) => {
        let broker = res.data[0];
        let controlResult = {
            status: 'SUCCESS',
            rawOutput: broker,
            resourceUuid: broker.brokerId,
        };

        console.log(broker.location)
        let locationExists = broker.location.indexOf("<pending>") === -1 &&
                                broker.location.indexOf("creating") === -1 ? true : false
        if(locationExists){
            console.log(JSON.stringify(controlResult, null, '\t'));
            return controlResult;
        }else{
            console.log('broker location not set, check again in 3 secs..')
            return new Promise((resolve, reject) => {
                setTimeout(() => resolve(_waitForLocation(broker.brokerId, uri)), 3000);
            });
        }
    })
}

module.exports.sendControl = sendControl;

