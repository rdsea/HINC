const axios = require('axios');
const config = require('../../config');

/**
 * gets the available resources from the provider
 */

function getItems(settings){
    return axios.get(`${config.ENDPOINT}/datatransformer/list`).then((res) => {
        let instances = res.data;
        let resources = [];
        instances.forEach((instance) => {
            resources.push(_noderedToResource(instance));
        });

        return resources;
    });
}

function _noderedToResource(instance){

    let ingressAccessPoint = {
        applicationProtocol: "HTTP",
        host: instance.location,
        port: 1880,
        accessPattern: "",
        networkProtocol: "IP",
    };

    let resource = {
        uuid: instance.datatransformerId,
        providerUuid: config.ADAPTOR_NAME,
        resourceType: 'CLOUD_SERVICE',
        name: `nodered instance`,
        controlPoints: [],
        dataPoints: [],
        type: 'SOFTWARE_ARTIFACT',
        location: null,
        parameters:{
            ingressAccessPoints:[ingressAccessPoint],
            egressAccessPoints: [],
        },
        metadata: {
            createdAt: instance.createdAt,
        },
    }
    return resource
}


module.exports.getItems = getItems;
