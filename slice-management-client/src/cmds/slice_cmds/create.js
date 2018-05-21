const meshService = require('../../services/mesh/mesh');
const amqpTools = require('../../amqpTools');
const db = require('../../data/db');
const moment = require('moment');
const path = require('path');

exports.command = 'create <file>'
exports.desc = 'creates a slice specified in <file>'
exports.builder = {}
exports.handler = function (argv) {
    let slice = require(path.join(process.cwd(), argv.file));
    return amqpTools.init().then(() => {
        return _provisionResources(slice)
    }).then(() => {
        console.log('slice successfully created!')
        amqpTools.close();
    });
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
        let msg = amqpTools.buildMessage('PROVISION', JSON.stringify(resource));
        return amqpTools.sendMessage(msg);
    }).then(() => {
        return amqpTools.getMessage(-1);
    }).then((msg) => {
        msg = JSON.parse(msg.content.toString());
        if(msg.msgType !== 'CONTROL_RESULT')
            throw new Error(`unexpected message reply received, expected CONTROL_RESULT but got ${msg.msgType}`);
        let provisionedResource = JSON.parse(msg.payload).rawOutput;
        console.log("resource provisioned: ");
        console.log(JSON.stringify(provisionedResource, null, 2));
        return {
            label,
            provisionedResource,
        };  
    });
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

// const testSlice = require('../../../test/testSlice.json');

// amqpTools.init().then(() => {
//     _provisionResources(testSlice).then((testSlice) => {
//         console.log(JSON.stringify(testSlice, null, 2));
//         amqpTools.close();
//     })
// })


