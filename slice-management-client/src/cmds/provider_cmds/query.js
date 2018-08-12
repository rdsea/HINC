const db = require('../../data/db');
const axios = require("axios");
const config = require("../../config");
const fs = require("fs");
const path = require("path");

exports.command = 'query <path>'
exports.desc = 'runs a search for resources matching the query in the json file <path>.'
exports.builder = {}
exports.handler = function (argv) {
    console.log(`searching resources, query located in the json file ${argv.path}`)  
    let query = JSON.parse(fs.readFileSync(path.join(process.cwd(), argv.path)));

    return axios.post(`${config.uri}/resourceproviders`, query).then((res) => {
        providers = res.data;
        _displayProviders(providers)
    })
    
}

function _displayProviders(providers){
    providers.forEach((provider, count) => {
        console.log(JSON.stringify(provider, null, 2));
        console.log('\n================================================\n')
    });    
    console.log(`retrieved ${providers.length} provider`)
}