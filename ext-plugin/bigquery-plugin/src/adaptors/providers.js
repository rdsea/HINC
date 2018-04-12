const axios = require('axios');
const config = require('../../config');


/**
 * gets the available resources provider information
 */
function getProvider(settings){
    return axios.get(`${config.ENDPOINT}/storage/bigquery`).then((res) => {
        let sampleConfig = res.data.sampleConfig;

        let managementPoints = [];
        managementPoints.push({
            name: `provision google bigquery dataset`,
            controlType: 'PROVISION',
            accessPoints: [{
                accessPointType: 'HTTP',
                uri: `${config.ENDPOINT}/storage/bigquery`,
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