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
    let resource = {
        uuid: ingestionClient.ingestionClientId,
        plugin: 'btsingestion',
        resourceType: 'CLOUD_SERVICE',
        name: `bts ingestion client`,
        controlPoints: [],
        dataPoints: [],
        type: 'SOFTWARE_UNIT',
        location: null,
        metadata: {
            ...ingestionClient,
        },
    }
    
    return resource;
}

module.exports.getItems = getItems;