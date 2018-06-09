const axios = require('axios');
const config = require('../../config')
const url = require("url");

/**
 * gets the available resources from the provider
 */
function getItems(){
    console.log('fetching sensor descriptions');
    return axios.get(`${config.ENDPOINT}/sensor/bts`).then((res) => {
        let sensorDescriptions = res.data;
        console.log(`${sensorDescriptions.length} sensor descriptions found`);

        let getSensors = [];
        sensorDescriptions.forEach((description) => {
            getSensors.push(axios.get(`${config.ENDPOINT}${description.url}`).then((res) => {
                console.log(`fetching sensors from ${description.url}`);
                return { description, sensors: res.data };
            }));
        });

        return Promise.all(getSensors);
    }).then((sensorItems) => {
        let resources = [];
        sensorItems.forEach((item) => {
            resources = resources.concat(_sensorItemToResources(item));
        });
        return resources;
    });
}

function _sensorItemToResources(item){
    let resources = [];

    item.sensors.forEach((sensor) => {
        let datapoint = {
            name: item.description.measurement,
            type: 'FLOAT',
            unit: item.description.unit,
        }
    
        let uri = url.parse(sensor.uri);
        let egressAccessPoint = {
            applicationProtocol: "MQTT",
            host: uri.hostname,
            port: parseInt(uri.port),
            accessPattern: "PUBSUB",
            networkProtocol: "IP",
            qos: 0,
            topics: [sensor.topic]
        };

        let resource = {
            uuid: sensor.clientId,
            providerUuid: config.ADAPTOR_NAME,
            plugin: 'btssensor',
            resourceType: 'IOT_RESOURCE',
            name: `sensor ${item.description.name}`,
            controlPoints: [],
            dataPoints: [datapoint],
            type: 'SENSOR',
            location: null,
            parameters:{
                ingressAccessPoints:[],
                egressAccessPoints: [egressAccessPoint],
            },
            metadata: {
                createdAt: sensor.createdAt,
            },
        }    
        resources.push(resource);
    });   
    return resources;
}

module.exports.getItems = getItems;