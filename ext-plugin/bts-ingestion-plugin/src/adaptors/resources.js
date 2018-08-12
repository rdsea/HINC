const axios = require('axios');
const config = require('../../config');

/**
 * gets the available resources from the provider
 */


function getItems(settings){
    console.log('fetching ingestion clients');
    return axios.get(`${config.ENDPOINT}/ingestionClient/list`).then((res) => {
        let ingestionClients = res.data;
        let resources = [];
        ingestionClients.forEach((ingestionClient) => {
            resources.push(_ingestionClientToResource(ingestionClient));
        });

        return resources;
    });
}

function _ingestionClientToResource(ingestionClient){
    let egressAccessPoints = [];
    if(ingestionClient.brokers){
        ingestionClient.brokers.forEach((broker) => {
            egressAccessPoints.push({
                applicationProtocol: "MQTT",
                host: broker.host,
                port: broker.port,
                username: broker.username,
                password: broker.password,
                accessPattern: "PUBSUB",
                networkProtocol: "IP",
                qos: 0,
                topics: broker.topics
            })
        })
    }
    

    let resource = {
        uuid: ingestionClient.ingestionClientId,
        plugin: 'btsingestion',
        resourceType: 'CLOUD_SERVICE',
        name: `bts ingestion client`,
        controlPoints: [],
        dataPoints: [],
        type: 'SOFTWARE_UNIT',
        location: null,
        parameters:{
            egressAccessPoints: egressAccessPoints,
            ingressAccessPoints: [],
        },
        metadata: {
            createdAt: ingestionClient.createdAt
        },
    }

    delete ingestionClient.brokers;
    resource.parameters = Object.assign(resource.parameters, ingestionClient)
    
    return resource;
}


module.exports.getItems = getItems;