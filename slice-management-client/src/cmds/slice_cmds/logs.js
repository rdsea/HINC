const db = require('../../data/db');
const config = require("../../config")
const axios = require('axios');

exports.command = 'logs <sliceId>'
exports.desc = 'output resource logs of slice <sliceId>'
exports.builder = {
    
}

exports.handler = function (argv) {
    let slice = null;
    db.sliceDao().findOne({sliceId: argv.sliceId}).then((s) => {
        if(!(s)) throw new Error(`could not find slice ${argv.sliceId}, please try again`);
        slice = s

        for(let label in slice.resources){
            axios.post(`${config.uri}/controls/logs`, slice.resources[label]).then((res) => {
                console.log(`${slice.resources[label].uuid} logs:`)
                console.log(JSON.stringify(res.data, null, 2));
            })
        }
        
    }).catch((err) => {
        console.err(err);
    })
}
