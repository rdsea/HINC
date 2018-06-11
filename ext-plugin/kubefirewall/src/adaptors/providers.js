const axios = require('axios');
const config = require('../../config');

function getProvider(){
    let availableResources = [];

    return axios.get(`${config.ENDPOINT}/kubefw`).then((res) => {
        let sampleConfig = res.data.sampleConfiguration;
        availableResources.push({
            resourceType: 'NETWORK_FUNCTION_SERVICE',
            name: `firewall`,
            providerUuid: config.ADAPTOR_NAME,
            controlPoints: [],
            dataPoints: [],
            type: 'FIREWALL',
            location: null,
            parameters:{
                ingressAccessPoints:[],
                egressAccessPoints: [],
            },
            metadata: {
                
            },
        });

        Object.assign(availableResources[0].parameters, sampleConfig);

        let provider = {
            name: config.ADAPTOR_NAME,
            uuid: config.ADAPTOR_NAME,
            availableResources: availableResources,
        };

        return provider;
    });
}

module.exports.getProvider = getProvider;