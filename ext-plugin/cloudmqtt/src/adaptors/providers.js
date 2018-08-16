const axios = require('axios');
const config = require('../../config');

function getProvider(){
    let availableResources = [];

    let ingressAccessPoint = {
        applicationProtocol: "MQTT",
        host: null,
        port: null,
        accessPattern: "PUBSUB",
        networkProtocol: "IP",
        qos: 0,
        topics: []
    };
    availableResources.push({
        providerUuid: config.ADAPTOR_NAME,
        resourceType: 'NETWORK_FUNCTION_SERVICE',
        name: `cloudmqtt broker`,
        controlPoints: [],
        dataPoints: [],
        type: 'BROKER',
        location: null,
        parameters:{
            ingressAccessPoints:[ingressAccessPoint],
            egressAccessPoints: [],
            name: "name",
        },
        metadata: {
            plan: "cat",
            region: "amazon-web-services::eu-west-1",
            apikey: "xxxxxx",
            name: "name"
        }
    });

    let provider = {
        name: config.ADAPTOR_NAME,
        uuid: config.ADAPTOR_NAME,
        availableResources: availableResources,
    };

    return new Promise((resolve, reject) => resolve(provider));
}

module.exports.getProvider = getProvider;