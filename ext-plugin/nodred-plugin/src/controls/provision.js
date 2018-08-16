const axios = require('axios');
const config = require('../../config');

function provision(resource){ 
    let controlResult = null;
    let instance = null;
    return axios.post(`${config.ENDPOINT}/datatransformer`).then((res) => {
        instance = res.data;
        resource.uuid = instance.datatransformerId;
        console.log('successful control execution');
        // location of instance might not set, await it
        return _waitForLocation(instance.datatransformerId, `${config.ENDPOINT}/datatransformer`);
    }).then((instanceIp) => {
        let ingressAccessPoint = {
            applicationProtocol: "HTTP",
            host: instanceIp,
            port: 1880,
            accessPattern: "",
            networkProtocol: "IP",
        };
        resource.parameters.ingressAccessPoints.push(ingressAccessPoint);

        let controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(resource),
            resourceUuid: resource.uuid,
        };
        return controlResult
    }).catch((err) => {
        console.log('control execution failed');
        console.error(err.response)
        controlResult = {
            status: 'FAILED',
            rawOutput: err.response,
        };
        return controlResult;
        throw err;
    });
}

function _waitForLocation(datatransformerId, uri){
    return axios.get(`${uri}/${datatransformerId}`).then((res) => {
        let instance = res.data[0];

        console.log(instance.location)
        let locationExists = instance.location.indexOf("<pending>") === -1 &&
                                instance.location.indexOf("creating") === -1 ? true : false
        if(locationExists){
            console.log(JSON.stringify(instance, null, '\t'));
            return instance.location;
        }else{
            console.log('instance location not set, check again in 3 secs..')
            return new Promise((resolve, reject) => {
                setTimeout(() => resolve(_waitForLocation(instance.datatransformerId, uri)), 3000);
            });
        }
    })
}

module.exports.provision = provision;

