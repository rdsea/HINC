const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');

exports.command = 'get <providerUuid>'
exports.desc = 'get details about provider <providerUuid>'
exports.builder = {}

exports.handler = function (argv) {
    let slice = null;
    console.log("TO BE IMPLEMENTED");
    /* old code
    db.providerDao().findOne({uuid: argv.providerUuid}).then((provider) => {
        if(!(provider)) throw new Error(`could not find provider ${argv.providerUuid}, please try again`);
        console.log(JSON.stringify(provider, null, 2));
    })
    */
}
