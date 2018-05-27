const meshService = require('../../services/mesh/mesh');
const amqpTools = require('../../amqpTools');
const db = require('../../data/db');
const moment = require('moment');
const path = require('path');
const axios = require('axios');
const config = require('../../config');

exports.command = 'create <file>'
exports.desc = 'creates a slice specified in <file>'
exports.builder = {}
exports.handler = function (argv) {
    let slice = require(path.join(process.cwd(), argv.file));
    return _provisionResources(slice).then(() => {
        slice.createdAt = moment().unix();
        db.sliceDao().insert(slice);
    })
}


function _provisionResources(slice){
    return _provisionMesh(slice).then(() => {
        let provisionResourcePromises = [];
        let provisionResourceItems = [];

        Object.keys(slice.resources).forEach((label) => {
            //provisionResourcePromises.push(_provisionResource(slice.sliceId, slice.resources[label], label));
            provisionResourceItems.push({
                sliceId: slice.sliceId,
                resource: slice.resources[label],
                label,
            });
        })

        return provisionResourceItems.reduce((promise, item) => {
            return promise.then((res) => {
                return _provisionResource(item.sliceId, item.resource, item.label).then((r) => res.concat(r));
            })
        }, Promise.resolve([]));

        //return Promise.all(provisionResourcePromises);
    }).then((provisionedResourceResults) => {
        // each resource is connected to the proxy as an egress endpoint
        let setNamePromises = [];

        // update slice resources with metadata afer provisioning
        provisionedResourceResults.forEach((provisionedResourceResult) => {
            Object.assign(slice.resources[provisionedResourceResult.label], provisionedResourceResult.provisionedResource);
        })

        for(let label in slice.connectivities){
            let inResource = slice.resources[slice.connectivities[label].in];
            let outResource = slice.resources[slice.connectivities[label].out];

            if(outResource.metadata.host && outResource.metadata.port)
                setNamePromises.push(meshService.setName(slice.sliceId, slice.connectivities[label].in, outResource.metadata.host, outResource.metadata.port));
        }

        return Promise.all(setNamePromises);
    }).then(() => {
        return meshService.flush(slice.sliceId);
    }).then(() => {
        return slice;
    })

}

function _provisionResource(sliceId, resource, label){
    return meshService.getProxyInfo(sliceId, label).then((proxy) => {
        // add the tcp ip endpoint into the resource metadata for the plugin
        resource.metadata._proxy = proxy;
        return axios.post(`${config.uri}/controls/provision`, resource);
    }).then((res) => {
        let provisionedResource = res.data
        console.log("resource provisioned: ");
        console.log(JSON.stringify(provisionedResource, null, 2));
        return {
            label,
            provisionedResource,
        };  
    }).catch((err) => {
        console.err(err.response);
    })
}


function _provisionMesh(slice){
    return meshService.createNameServer(slice.sliceId).then(() => {
        let proxyCreatePromises = [];
        for(let label in slice.resources){
            let proxyCreatePromise = meshService.createProxy(slice.sliceId, label);
            proxyCreatePromises.push(proxyCreatePromise);
        }
        
        return Promise.all(proxyCreatePromises);
    });
}


