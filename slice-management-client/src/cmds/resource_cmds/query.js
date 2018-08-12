const db = require('../../data/db');
const fs = require("fs");
const path = require("path");
const axios = require("axios");

exports.command = 'query <path>'
exports.desc = 'runs a query for resources matching the query in the json file <path>.'
exports.builder = {}
exports.handler = function (argv) {
    console.log(`querying resources, query located in the json file ${argv.path}`)  
    let query = JSON.parse(fs.readFileSync(path.join(process.cwd(), argv.path)));

    return axios.post(`${config.uri}/resourceproviders`, query).then((res) => {
        resources = res.data;
        _displayResources(resources);
    })
    
}

function _displayResources(resources){
    resources.forEach((resource, count) => {
        console.log(JSON.stringify(resource, null, 2));
        console.log('\n================================================\n')
    });    
    console.log(`retrieved ${resources.length} resources`)
}