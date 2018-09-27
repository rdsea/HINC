const axios = require('axios');

var mqttplugin_config = require('config');
var config = mqttplugin_config.get('mqttadaptor');

function provision(resource){
    let controlResult = null;
    let broker = null;
    return axios.post(`${config.ENDPOINT}/mosquittobroker`).then((res) => {
        broker = res.data;
        resource.uuid = broker.brokerId;
        console.log('successful control execution');
        // location of broker might not set, await it
        console.log("waiting for IP assignment")
        return _waitForLocation(broker.brokerId, `${config.ENDPOINT}/mosquittobroker`);
    }).then((brokerIp) => {
        let ingressAccessPoint = {
            applicationProtocol: "MQTT",
            host: brokerIp,
            port: 1883,
            accessPattern: "PUBSUB",
            networkProtocol: "IP",
            qos: 0,
            topics: []
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
        console.error(err)
        controlResult = {
            status: 'FAILED',
            rawOutput: err.response,
        };
        return controlResult;
        throw err;
    });
}

function _waitForLocation(brokerId, uri){
    return axios.get(`${uri}/${brokerId}`).then((res) => {
        let broker = res.data[0];
        let locationExists = broker.location.indexOf("<pending>") === -1 &&
                                broker.location.indexOf("creating") === -1 ? true : false
        if(locationExists){
            console.log(JSON.stringify(broker, null, '\t'));
            return broker.location;
        }else{
            //console.log('broker location not set, check again in 3 secs..')
            return new Promise((resolve, reject) => {
                setTimeout(() => resolve(_waitForLocation(broker.brokerId, uri)), 3000);
            });
        }
    })
}


module.exports.provision = provision;
