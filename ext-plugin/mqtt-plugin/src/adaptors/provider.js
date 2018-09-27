const axios = require('axios');

var mqttplugin_config = require('config');
var config = mqttplugin_config.get('mqttadaptor');

/**
 * gets the available resources provider information
 */
function getProvider(settings){
    let availableResources = [];

    availableResources.push({
        resourceType: 'NETWORK_FUNCTION_SERVICE',
        name: `mosquitto broker`,
        providerUuid: config.ADAPTOR_NAME,
        controlPoints: [],
        dataPoints: [],
        type: 'BROKER',
        location: null,
        parameters:{
            ingressAccessPoints:[],
            egressAccessPoints: [],
        },
        metadata: {

        },
    });

    provider = {
        name: config.ADAPTOR_NAME,
        uuid: config.ADAPTOR_NAME,
        availableResources: availableResources,
    };
    return new Promise((resolve, reject) => resolve(provider));
}

module.exports.getProvider = getProvider;
