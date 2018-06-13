const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');
const config = require("../../config")
const axios = require('axios');
const meshService = require("../../services/mesh/mesh")
const path = require("path");
const fs = require('fs');

exports.command = 'update <file>'
exports.desc = 'updates slice specified in <file>'
exports.builder = {}

exports.handler = function (argv){
    let filepath = path.resolve(argv.file);
    let newSlice = require(filepath);
    let slice = null;
    let resourceChanges = [];
    let connectivityChanges = [];
    db.sliceDao().findOne({sliceId: newSlice.sliceId}).then((s) => {
        if(!(s)) throw new Error(`could not find slice ${newSlice.sliceId}, an update must be for an existing slice!`);
        slice = s;

        resourceChanges = _identifyModifiedResouces(slice, newSlice);
        connectivityChanges = _identifyModifiedConnectivities(slice, newSlice);

        // provision new resources
        let provisionResourcePromises = [];
        resourceChanges.new.forEach((newResourceLabel) => {
            let provisionResourcePromise = _provisionResource(slice.sliceId, newSlice.resources[newResourceLabel], newResourceLabel);
            provisionResourcePromises.push(provisionResourcePromise);
        });

        return Promise.all(provisionResourcePromises);
    }).then((provisionedResourceResults) => {
        // update slice resources with metadata afer provisioning
        provisionedResourceResults.forEach((provisionedResourceResult) => {
            Object.assign(newSlice.resources[provisionedResourceResult.label], provisionedResourceResult.provisionedResource);
        });

        // configure modified resources
        let configureResourcePromises = [];
        resourceChanges.modified.forEach((modifiedResourceLabel) => {
            let configuredResourcePromise = _configureResource(slice.sliceId, newSlice.resources[modifiedResourceLabel], modifiedResourceLabel);
            configureResourcePromises.push(configuredResourcePromise);
        });

        return Promise.all(configureResourcePromises);
    }).then((configuredResourceResults) => {
        // update slice resources with metadata afer provisioning
        configuredResourceResults.forEach((configuredResourceResult) => {
            Object.assign(newSlice.resources[configuredResourceResult.label], configuredResourceResult.configuredResource);
        });

        console.debug("connecting resources..");
        let nameObjs = [];
        for(let label in newSlice.connectivities){
            let inResource = newSlice.resources[newSlice.connectivities[label].in.label];
            let outResource = newSlice.resources[newSlice.connectivities[label].out.label];
            nameObjs.push(_connectResources(newSlice.sliceId, newSlice.connectivities[label], inResource, outResource));
        }

        return meshService.setNames(newSlice.sliceId, nameObjs);
    }).then(() => {
        return db.sliceDao().update({sliceId: newSlice.sliceId}, newSlice, {});
    }).then(() => {
        console.log(`slice ${newSlice.sliceId} successfully updated`)
        console.log(`writing slice deployment to ${filepath}`);
        fs.writeFileSync(filepath, JSON.stringify(newSlice, null, 4));
    });
}


function _provisionResource(sliceId, resource, label){
    return meshService.createProxy(sliceId, label, resource.parameters.egressAccessPoints.length).then((proxy) => {
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

function _configureResource(sliceId, resource, label){
    console.debug(`configuring ${label}`);
    return axios.post(`${config.uri}/controls/configure`, resource).then((res) => {
        let configuredResource = res.data;
        console.log("resource configured: ");
        console.log(JSON.stringify(configuredResource, null, 2));
        return {
            label,
            configuredResource,
        }
    })
}

function _identifyModifiedResouces(slice, newSlice){
    let modifiedResources = [];
    let newResources = [];

    for(let label in newSlice.resources){
        if(!(slice.resources[label])){
            newResources.push(label);
        }else{
            let currentResourceParams = Object.assign({}, slice.resources[label].parameters);
            let newResourceParams = Object.assign({}, newSlice.resources[label].parameters);
    
            if(JSON.stringify(currentResourceParams) !== JSON.stringify(newResourceParams)){
                modifiedResources.push(label);
            }
        }        
    }

    return { new: newResources, modified: modifiedResources };
}

function _identifyModifiedConnectivities(slice, newSlice){
    let modifiedConnectivities = [];
    let newConnectivities = [];

    for(let label in newSlice.connectivities){
        if((!slice.connectivities[label])){
            newConnectivities.push(label);
        }else{
            let currentConnectivity = JSON.stringify(slice.connectivities[label]);
            let newConnectivity = JSON.stringify(newSlice.connectivities[label]);

            if(currentConnectivity !== newConnectivity){
                modifiedConnectivities.push(label);
            }
        }
    }

    return { new: newConnectivities, modified: modifiedConnectivities };
}

function _connectResources(sliceId, connectivity, inResource, outResource){
    console.log(JSON.stringify(outResource));
    console.log(JSON.stringify(connectivity));
    let outAccessPoint = outResource.parameters.ingressAccessPoints[connectivity.out.accessPoint];
    let name = `${connectivity.in.label}${connectivity.in.accessPoint}`
    return {
        name: `${connectivity.in.label}${connectivity.in.accessPoint}`,
        host: outAccessPoint.host,
        port: outAccessPoint.port,
    }
}