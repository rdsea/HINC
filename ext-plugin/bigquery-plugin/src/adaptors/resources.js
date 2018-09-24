const axios = require('axios');
//onst config = require('../../config');
var bigqueryplugin_config = require('config');
var config = bigqueryplugin_config.get('bigqueryadaptor');
/**
 * gets the available resources from the provider
 */


function getItems(settings){
    console.log('fetching bigQuery datasets');
    return axios.get(`${config.ENDPOINT}/storage/bigquery/list`).then((res) => {
        let datasets = res.data;
        let resources = [];
        datasets.forEach((dataset) => {
            resources.push(_datasetToResource(dataset));
        });

        return resources;
    });
}

function _datasetToResource(dataset){
    let resource = {
        uuid: dataset.datasetId,
        plugin: 'bigquery',
        resourceType: 'CLOUD_SERVICE',
        name: `bigQuery dataset`,
        controlPoints: [],
        dataPoints: [],
        type: 'SOFTWARE_UNIT',
        location: null,
        parameters: {
            ingressAccessPoints:[],
            egressAccessPoints: [],
        },
        metadata: {},
    }

    resource.parameters = Object.apply(resource.parameters, dataset);
    return resource;
}

module.exports.getItems = getItems;
