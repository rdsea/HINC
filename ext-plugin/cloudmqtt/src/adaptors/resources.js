const axios = require('axios');
const config = require('../../config');
const parse = require("url-parse");

function getItems(){
    console.log("Obtaining resources from the provider ",config.ENDPOINT);
    return axios.get(`${config.ENDPOINT}`, {auth: { username: process.env.CLOUDMQTT_KEY}}).then((res) => {
        let items = res.data;
        let getBrokerPromises = [];
        items.forEach((item) => {
            getBrokerPromises.push(axios.get(`${config.ENDPOINT}/${item.id}`, {auth: { username: process.env.CLOUDMQTT_KEY}}).then(res => res.data));
        })
        return Promise.all(getBrokerPromises);
    }).then((brokers) => {
        let resources = [];


        brokers.forEach((broker) => {
            let brokerUrl = parse(broker.url);
            let ingressAccessPoint = {
                applicationProtocol: "MQTT",
                host: brokerUrl.hostname,
                port: brokerUrl.port,
                usename: brokerUrl.username,
                password: brokerUrl.password,
                accessPattern: "PUBSUB",
                networkProtocol: "IP",
                qos: 0,
                topics: []
            };

            resources.push({
                uuid: broker.id,
                providerUuid: config.ADAPTOR_NAME,
                resourceType: 'NETWORK_FUNCTION_SERVICE',
                name: `cloudmqtt broker`,
                controlPoints: [
                ],
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

getItems().then((res) => console.log(res));
module.exports.getItems = getItems;
