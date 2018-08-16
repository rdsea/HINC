const axios = require('axios');
const config = require('../../config');


/**
 * gets the available resources provider information
 */
function getProvider(settings){
    let provider = null;
    return axios.get(`${config.ENDPOINT}/sensor/bts`).then((res) => {
        let sensorDescriptions = res.data;
        let availableResources = [];
        sensorDescriptions.forEach((description) => {
            availableResources.push({
                plugin: 'btssensor',
                providerUuid: config.ADAPTOR_NAME,
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

                parameters:{
                    ingressAccessPoints:[],
                    egressAccessPoints: [{
                        applicationProtocol: "MQTT",
                        host: "target host",
                        port: "targetport",
                        username: "xxx",
                        password: "xxx",
                        accessPattern: "PUBSUB",
                        networkProtocol: "IP",
                        qos: 0,
                        topics: ["topic1", "topic2"]
                    }],
                },
                metadata: {
                },
            }    )
        });

        let provider = {
            name: config.ADAPTOR_NAME,
            uuid: config.ADAPTOR_NAME,
            availableResources: availableResources,
        };
        return provider;       
    });
}

module.exports.getProvider = getProvider;