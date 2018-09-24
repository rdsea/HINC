const axios = require('axios');

var noderedplugin_config = require('config');
var config = noderedplugin_config.get('noderedadaptor');
/**
 * gets the available resources provider information
 */
function getProvider(settings){
    let availableResources = [];

    availableResources.push({
        resourceType: 'CLOUD_SERVICE',
        name: `nodered instance`,
        providerUuid: config.ADAPTOR_NAME,
        controlPoints: [],
        dataPoints: [],
        resourceCategory: 'SOFTWARE_ARTIFACT',
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
