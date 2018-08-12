const axios = require('axios');
const config = require('../../config');
const parse = require("url-parse");

function getItems(){
    console.log("fetching amqp brokers");

    return axios.get(`${config.ENDPOINT}`, {auth: { username: process.env.MQTT_KEY}}).then((res) => {
        let items = res.data;
        let getBrokerPromises = [];
        items.forEach((item) => {
            getBrokerPromises.push(axios.get(`${config.ENDPOINT}/${item.id}`, {auth: { username: process.env.MQTT_KEY}}).then(res => res.data));
        })
        return Promise.all(getBrokerPromises);
    }).then((brokers) => {
        let resources = [];

        let brokerUrl = parse(broker.url);
        brokers.forEach((broker) => {
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

