//const amqpTools = require('../../amqpTools');
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
}

exports.handler = function (argv) {
    //let payload = argv.limit ? { limit: argv.limit } : '';
    //let query = amqpTools.buildMessage('FETCH_PROVIDERS', payload);

    return axios.get(`${config.uri}/resourceproviders`).then((res) => {
        let providers = res.data;
        _displayProviders(providers)
    }).catch((err) => {
        console.err(err);
    })


}


function _displayProviders(providers){
    providers.forEach((provider, count) => {
        console.log(JSON.stringify(provider, null, 2));
        console.log('\n================================================\n')
    });
    console.log(`retrieved ${providers.length} providers`)
}
