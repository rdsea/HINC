const axios = require('axios');
const config = require('../../config');
const url = require("url");

function getItems(){
    console.log("Obtaining resources from the provider");

    return axios.get(`${config.ENDPOINT}`, {auth: { username: process.env.CLOUDAMQP_KEY}}).then((res) => {
        let items = res.data;
        let getBrokerPromises = [];
        items.forEach((item) => {
            getBrokerPromises.push(axios.get(`${config.ENDPOINT}/${item.id}`, {auth: { username: process.env.CLOUDAMQP_KEY}}).then(res => res.data));
        })
        return Promise.all(getBrokerPromises);
    }).then((brokers) => {
        let resources = [];

        brokers.forEach((broker) => {
            let ingressAccessPoint = {
                applicationProtocol: "AMQP",
                host: broker.url,
                port: null,
                accessPattern: "PUBSUB",
                networkProtocol: "IP",
                qos: 0,
                topics: []
            };

            resources.push({
                uuid: broker.id,
                providerUuid: config.ADAPTOR_NAME,
                resourceType: 'NETWORK_FUNCTION_SERVICE',
                name: `rabbitmq broker`,
                controlPoints: [],
                dataPoints: [],
                type: 'BROKER',
                location: null,
                parameters:{
                    ingressAccessPoints:[ingressAccessPoint],
                    egressAccessPoints: [],
                },
                metadata: {
                    plan: broker.plan,
                    region: broker.region,
                    apikey: broker.apikey,
                    name: broker.name
                },
            })
        });

        return resources;
    })
}

module.exports.getItems = getItems;
