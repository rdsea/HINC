const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');

exports.command = 'get <resourceUuid>'
exports.desc = 'get details about resource <resourceUuid>'
exports.builder = {}

exports.handler = function (argv) {
    let slice = null;
    console.log("TO BE IMPLEMENTED");
    /*refactor old code
    db.resourceDao().findOne({uuid: argv.resourceUuid}).then((resource) => {
        if(!(resource)) throw new Error(`could not find resource ${argv.resourceUuid}, please try again`);
        console.log(JSON.stringify(resource, null, 2));
    })
    */
}
