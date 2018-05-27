const amqpTools = require('../../amqpTools');
const db = require('../../data/db');
const axios = require('axios');
const config = require('../../config');

exports.command = 'list'
exports.desc = 'list available resource providers'
exports.builder = {
    limit:{
        alias: 'l',
        describe: 'limit the number of results',
        type: 'number'
    },
    refresh:{
        alias: 'r',
        describe: 'refresh data from datasource',
        type: 'boolean',
        demandOption: false
    }
}

exports.handler = function (argv) {
    let payload = argv.limit ? { limit: argv.limit } : '';
    let query = amqpTools.buildMessage('FETCH_PROVIDERS', payload);

    if(!(argv.refresh)){
        return db.providerDao().find({}).then((resources) => {
            _displayProviders(resources);
        });
    }else{
        return axios.get(`${config.uri}/resourceproviders`).then((res) => {
            let providers = res.data;
            return _refreshProviders(providers);
        }).then((providers) => {
            console.log(`retrieved ${providers.length} providers`)
            _displayProviders(providers)
        }).catch((err) => {
            console.err(err);
        })
    }

    
}

function _refreshProviders(providers){
    return db.providerDao().remove({}, {multi: true}).then(() => {
        let promises = [];
        providers.forEach((provider) => {
        promises.push(db.providerDao().insert(provider));
    });
        return Promise.all(promises);
    }).then(() => {
        return providers;
    });    
}



function _displayProviders(providers){
    providers.forEach((provider, count) => {
        console.log(JSON.stringify(provider, null, 2));
        console.log('\n================================================\n')
    });    
    console.log(`retrieved ${providers.length} providers`)
}