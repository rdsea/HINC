const axios = require('axios');
const config = require('../../config');
const qs = require("qs");
const parse = require("url-parse");

function provision(resource){
    let controlResult = null;
    let broker = null;

    let provisionParameters = {
        name: resource.parameters.name,
        plan: "cat",
        region: "amazon-web-services::eu-west-1",
    }

    let options = {
        auth: { username: process.env.MQTT_KEY},
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    }

    return _getAvailableBrokers().then((brokers) => {
        if(brokers.length < 5){
            return axios.post(`${config.ENDPOINT}`, qs.stringify(provisionParameters), options).catch((err) => {
                console.error(err)
                if(brokers.length > 0) return brokers[Math.floor(Math.random()*brokers.length)];
                
                throw err;
            });
        }else{
            return brokers[Math.floor(Math.random()*brokers.length)];
        }
    }).then((broker) => {
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

        resource.parameters.ingressAccessPoints[0] = ingressAccessPoint;
        resource.metadata = {
            plan: broker.plan,
            region: broker.region,
            apikey: broker.apikey,
            name: broker.name,
            url: broker.url,
        }
        resource.uuid = broker.id;
                    
        let controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(resource),
            resourceUuid: resource.uuid,
        };
        console.log(controlResult);
        return controlResult

    }).catch((err) => {
        console.log('control execution failed');
        console.error(err.response)
        controlResult = {
            status: 'FAILED',
            rawOutput: err.response,
        };
        return controlResult;
    });
}

function _getAvailableBrokers(){
    return axios.get(`${config.ENDPOINT}`, {auth: { username: process.env.MQTT_KEY}}).then((res) => {
        let items = res.data;
        let getBrokerPromises = [];
        items.forEach((item) => {
            getBrokerPromises.push(axios.get(`${config.ENDPOINT}/${item.id}`, {auth: { username: process.env.MQTT_KEY}}).then(res => res.data));
        })
        return Promise.all(getBrokerPromises);
    })
}


module.exports.provision = provision;