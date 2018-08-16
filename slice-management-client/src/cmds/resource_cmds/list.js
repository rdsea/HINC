const amqpTools = require('../../amqpTools');
const db = require('../../data/db');
const axios = require('axios');
const config = require('../../config');


exports.command = 'list [options]'
exports.desc = 'list available resources'
exports.builder = {
    limit:{
        alias: 'l',
        describe: 'limit the number of results',
        type: 'number',
        demandOption: false
    },
}

exports.handler = function (argv) {
    let payload = argv.limit ? { limit: argv.limit } : '';
    let query = amqpTools.buildMessage('FETCH_RESOURCES', payload);


    return axios.get(`${config.uri}/resources`).then((res) => {
        let resources = res.data;
        _displayResources(resources)
    }).catch((err) => {
        console.err(err);
    })

}

function _displayResources(resources){
    resources.forEach((resource, count) => {
        console.log(JSON.stringify(resource, null, 2));
        console.log('\n================================================\n')
    });    
    console.log(`retrieved ${resources.length} resources`)
}