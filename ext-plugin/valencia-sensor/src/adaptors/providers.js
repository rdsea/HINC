const axios = require('axios');
const config = require('../../config');

/**
 * gets the available resources provider information
 */
function getProvider(settings){
    let provider = null;
    return axios.get(`${config.ENDPOINT}/sensor/valencia`).then((res) => {
        let sensorDescriptions = res.data;
        let availableResources = [];
        sensorDescriptions.forEach((description) => {
            availableResources.push({
                providerUuid: config.ADAPTOR_NAME,
                resourceType: 'IOT_RESOURCE',
                name: `${description.name}`,
                controlPoints: [],
                dataPoints: [{
                    name: description.measurement,
                    unit: description.unit,
                    dataType: 'FLOAT'
                }],
                type: 'SENSOR',
                location: null,

                parameters:{
                    ingressAccessPoints:[],
                    egressAccessPoints: [{
                        applicationProtocol: "MQTT",
                        host: "target host",
                        port: "targetport",
                        accessPattern: "PUBSUB",
                        networkProtocol: "IP",
                        qos: 0,
                        topics: ["topic1", "topic2"]
                    }],
                },
                metadata: {
                },
            })
        })
        provider = {
            name: config.ADAPTOR_NAME,
            uuid: config.ADAPTOR_NAME,
            availableResources: availableResources,
        };
        return provider;    
    })
    
}

module.exports.getProvider = getProvider;