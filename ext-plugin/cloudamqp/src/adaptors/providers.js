const axios = require('axios');
const config = require('../../config');

function getProvider(){
    let availableResources = [];

    let ingressAccessPoint = {
        applicationProtocol: "AMQP",
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
        name: `rabbitmq broker`,
        controlPoints: [],
        dataPoints: [],
        type: 'BROKER',
        location: null,
        parameters:{
            ingressAccessPoints:[ingressAccessPoint],
            egressAccessPoints: [],
            name: "name",
        },
        metadata: {}
    });

    let provider = {
        name: config.ADAPTOR_NAME,
        uuid: config.ADAPTOR_NAME,
        availableResources: availableResources,
    };

    return new Promise((resolve, reject) => resolve(provider));
}

module.exports.getProvider = getProvider;