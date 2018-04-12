const axios = require('axios');
const config = require('../../config');

/**
 * gets the available resources from the provider
 */

function getItems(settings){
    return axios.get(`${config.ENDPOINT}/mosquittobroker/list`).then((res) => {
        let brokers = res.data;
        let resources = [];
        brokers.forEach((broker) => {
            resources.push(_brokerToResource(broker));
        });

        return resources;
    });
}

function _brokerToResource(broker){

    let resource = {
        uuid: broker.brokerId,
        plugin: 'mosquittobroker',
        resourceType: 'NETWORK_FUNCTION_SERVICE',
        name: `mosquitto broker`,
        controlPoints: [],
        dataPoints: [],
        type: 'BROKER',
        location: null,
        metadata: {
            brokerId: broker.brokerId,
            uri: broker.location,
            createdAt: broker.createdAt,
        },
    }
    return resource
}

module.exports.getItems = getItems;
