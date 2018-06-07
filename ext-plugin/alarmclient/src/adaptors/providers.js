const axios = require('axios');
const config = require('../../config');


/**
 * gets the available resources provider information
 */

 function getProvider(){
     return axios.get(`${config.ENDPOINT}/alarmclient`).then((res) => {
         let sampleConfig = res.data.sampleConfiguration;
         let availableResources = [];

         availableResources.push({
            providerUuid: config.ADAPTOR_NAME,
            resourceType: 'CLOUD_SERVICE',
            name: `alarmclient`,
            controlPoints: [],
            dataPoints: [],
            type: 'SOFTWARE_UNIT',
            location: null,
            parameters:{
                egressAccessPoints: [{
                    applicationProtocol: "MQTT",
                    host: "alarm broker host",
                    port: "alarm broker port",
                    accessPattern: "PUBSUB",
                    networkProtocol: "IP",
                    qos: 0,
                    topics: ["topic1", "topic2"]
                },
                {
                    applicationProtocol: "MQTT",
                    host: "vessel broker host",
                    port: "vessel broker port",
                    accessPattern: "PUBSUB",
                    networkProtocol: "IP",
                    qos: 0,
                    topics: ["vessel1", "vessel2"]
                }],
                ingressAccessPoints: [],    
                pcs: "http://localhost:8888",
            },
            metadata: {
                
            },
        })
        let provider = {
            name: config.ADAPTOR_NAME,
            uuid: config.ADAPTOR_NAME,
            availableResources: availableResources,
        };

        return provider;
     })
 }

 module.exports.getProvider = getProvider;

 getProvider().then(provider => console.log(JSON.stringify(provider, null, 2)))