const axios = require('axios');
const config = require('../../config');

/**
 * gets the available resources provider information
 */
function getProvider(settings){
    let availableResources = [];

    availableResources.push({
        resourceType: 'NETWORK_FUNCTION_SERVICE',
        name: `mosquitto broker`,
        controlPoints: [],
        dataPoints: [],
        type: 'BROKER',
        location: null,
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