const axios = require('axios');
const config = require('../../config');


/**
 * gets the available resources provider information
 */
function getProvider(settings){
    let provider = null;
    return axios.get(`${config.ENDPOINT}/sensor/bts`).then((res) => {
        let sensorDescriptions = res.data;
        let managementPoints = [];
        let availableResources = [];
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

            availableResources.push({
                plugin: 'btssensor',
                resourceType: 'IOT_RESOURCE',
                name: `sensor ${description.name}`,
                controlPoints: [],
                dataPoints: [{
                    name: description.measurement,
                    unit: description.unit,
                    dataType: 'FLOAT'
                }],
                type: 'SENSOR',
                location: null,
                metadata: {
                    communication: description.communication,
                    format: description.format,
                    parameters:{
                        topic: description.topic,
                    }
                },
            }    )
        });

        let provider = {
            name: config.ADAPTOR_NAME,
            uuid: config.ADAPTOR_NAME,
            availableResources: availableResources,
            managementPoints: managementPoints,
        };
        return provider;       
    });
}

module.exports.getProvider = getProvider;