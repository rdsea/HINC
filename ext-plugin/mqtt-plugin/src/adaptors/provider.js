const axios = require('axios');
const config = require('../../config');

/**
 * gets the available resources provider information
 */
function getProvider(settings){
    let managementPoints = [];

    managementPoints.push({
        name: `provision mosquitto broker`,
        controlType: 'PROVISION',
        accessPoints: [{
            accessPointType: 'HTTP',
            uri: `${config.ENDPOINT}/mosquittobroker/`,
            httpMethod: 'POST',
        }],
        parameters: {},   
    });

    provider = {
        name: config.ADAPTOR_NAME,
        uuid: config.ADAPTOR_NAME,
        managementPoints: managementPoints,
    };
    return new Promise((resolve, reject) => resolve(provider));
}

module.exports.getProvider = getProvider;