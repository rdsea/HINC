const meshService = require('../../services/mesh/mesh');
const amqpTools = require('../../amqpTools');
const db = require('../../data/db');
const moment = require('moment');
const path = require('path');
const axios = require('axios');
const config = require('../../config');
const fs = require("fs");

exports.command = 'create <file>'
exports.desc = 'creates a slice specified in <file>'
exports.builder = {}
exports.handler = function (argv) {
    let filepath = path.resolve(argv.file);
    let slice = require(filepath);
    return _provisionResources(slice).then(() => {
        slice.createdAt = moment().unix();
        return db.sliceDao().insert(slice);
    }).then((slice) => {
        console.log(`writing slice deployment to ${filepath}.provisioned`);
        fs.writeFileSync(`${filepath}  `, JSON.stringify(slice, null, 4));
    })
}


function _provisionResources(slice){
    console.log("provisioning service mesh");
    return _provisionMesh(slice).then(() => {
        console.log("provisioned service mesh");
        console.log("provisioning resources");
        let provisionResourcePromises = [];
        let provisionResourceItems = [];

        Object.keys(slice.resources).forEach((label) => {
            provisionResourcePromises.push(_provisionResource(slice.sliceId, slice.resources[label], label));
        })
        return Promise.all(provisionResourcePromises);
    }).then((provisionedResourceResults) => {
        // update slice resources with metadata afer provisioning
        provisionedResourceResults.forEach((provisionedResourceResult) => {
            Object.assign(slice.resources[provisionedResourceResult.label], provisionedResourceResult.provisionedResource);
        })

        // each resource is connected to the proxy as an egress endpoint
        let nameObjs = [];

        for(let label in slice.connectivities){
            let inResource = slice.resources[slice.connectivities[label].in.label];
            let outResource = slice.resources[slice.connectivities[label].out.label];
            nameObjs.push(_connectResources(slice.sliceId, slice.connectivities[label], inResource, outResource));
        }
        console.log("connecting resources")
        return meshService.setNames(slice.sliceId, nameObjs);
    }).then(() => {
        return slice;
    })

}

function _connectResources(sliceId, connectivity, inResource, outResource){
    let outAccessPoint = outResource.parameters.ingressAccessPoints[connectivity.out.accessPoint];
    let name = `${connectivity.in.label}${connectivity.in.accessPoint}`
    return {
        name: `${connectivity.in.label}${connectivity.in.accessPoint}`,
        host: outAccessPoint.host,
        port: outAccessPoint.port,
    }
}

function _provisionResource(sliceId, resource, label){
    return meshService.getProxyInfo(sliceId, label).then((proxy) => {
        // add the tcp ip endpoint into the resource metadata for the plugin
        for(let i=0;i<resource.parameters.egressAccessPoints.length;i++){
            resource.parameters.egressAccessPoints[i].host = proxy.ip;
            resource.parameters.egressAccessPoints[i].port = proxy.ports[i];
        }
        console.debug(`provisioning ${label}`);
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
            let proxyCreatePromise = meshService.createProxy(slice.sliceId, label, slice.resources[label].parameters.egressAccessPoints.length);
            proxyCreatePromises.push(proxyCreatePromise);
        }
        
        return Promise.all(proxyCreatePromises);
    });
}


