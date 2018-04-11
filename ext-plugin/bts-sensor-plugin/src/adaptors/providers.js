const axios = require('axios');
const config = require('../../config');

var resourceProvider = {
    name: 'iot unit resource provider',
    managementPoints: [],
    resources: [],
}

/**
 * gets the available resources provider information
 */
function getProvider(settings){
    let provider = null;
    return axios.get(`${config.ENDPOINT}/sensor/bts`).then((res) => {
        let sensorDescriptions = res.data;
        let managementPoints = [];
        sensorDescriptions.forEach((description) => {
            managementPoints.push({
                name: `provision bts ${description.name} sensor`,
                controlType: 'PROVISION',
                accessPoints: [{
                    accessPointType: 'HTTP',
                    uri: `${config.ENDPOINT}${description.url}`,
                    httpMethod: 'POST',
                }],
                parameters: description.sampleConfiguration,   
            });
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