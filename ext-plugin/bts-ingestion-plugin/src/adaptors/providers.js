const axios = require('axios');
const config = require('../../config');


/**
 * gets the available resources provider information
 */
function getProvider(settings){
    return axios.get(`${config.ENDPOINT}/ingestionClient`).then((res) => {
        let sampleConfig = res.data.sampleConfiguration;

        let managementPoints = [];
        managementPoints.push({
            name: `provision bts ingestion client`,
            controlType: 'PROVISION',
            accessPoints: [{
                accessPointType: 'HTTP',
                uri: `${config.ENDPOINT}/ingestionClient`,
                httpMethod: 'POST',
            }],
            parameters: sampleConfig,   
        });

        let provider = {
            name: config.ADAPTOR_NAME,
            uuid: config.ADAPTOR_NAME,
            managementPoints: managementPoints,
        };

        return provider;
    });
}

module.exports.getProvider = getProvider;
