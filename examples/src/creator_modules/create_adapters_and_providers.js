const fs = require('fs');
const path = require("path");
const properties = require('properties');

const createNodeRed = require('./adapters_and_providers/create_nodered-datatransformer-provider');


module.exports = {
    create:createAllAdaptorsAndProviders,
    add:addAdapterProviderCreator
}

let adapterProviderCreators = [];

function createAllAdaptorsAndProviders(config, compositionConfig){
    compositionConfig.localConfigs.forEach((localConfig) => {
        createAdaptorsAndProviders(config, compositionConfig, localConfig);
    })
}

function createAdaptorsAndProviders(config, compositionConfig, localConfig){
    adapterProviderCreators.forEach((apCreator) => {
        apCreator.create(config,compositionConfig,localConfig);
    });
    //createNodeRed.create(config,compositionConfig,localConfig);
}

function addAdapterProviderCreator(creator) {
    adapterProviderCreators.push(creator);
}
