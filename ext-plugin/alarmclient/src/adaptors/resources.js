const axios = require('axios');
const config = require('../../config');

/**
 * gets the available resources from the provider
 */


function getItems(settings){
    console.log("fetching alarm clients");
    return axios.get(`${config.ENDPOINT}/alarmclient/list`).then((res) => {
        let alarmClients = res.data;
        let resources = [];
        alarmClients.forEach((alarmClient) => {
            resources.push({
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
                        host: alarmClient.alarmBroker.host,
                        port: alarmClient.alarmBroker.port,
                        accessPattern: "PUBSUB",
                        networkProtocol: "IP",
                        qos: 0,
                        topics: alarmClient.alarmBroker.topics
                    },
                    {
                        applicationProtocol: "MQTT",
                        host: alarmClient.vesselBroker.host,
                        port: alarmClient.vesselBroker.port,
                        accessPattern: "PUBSUB",
                        networkProtocol: "IP",
                        qos: 0,
                        topics: alarmClient.vesselBroker.topics
                    }],
                    ingressAccessPoints: [],    
                    pcs: alarmClient.pcs,
                },
                metadata: {
                    
                },
            })
        })
    })
}

module.exports.getItems = getItems;

getItems().then(resources => console.log(JSON.stringify(resources, null, 2)))