const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');

exports.command = 'add <sliceId> <resourceUuid>'
exports.desc = 'add resource <resourceUuid> to slice <sliceId>'
exports.builder = {}

exports.handler = function (argv) {
    console.log(`add resource ${argv.resourceUuid} to slice ${argv.sliceId}`);
    let slice = null;
    // TODO add more information to this output
    let searches = [];
    searches.push(db.sliceDao().findOne({sliceId: argv.sliceId}))
    searches.push(db.resourceDao().findOne({uuid: argv.resourceUuid}));
    return Promise.all(searches).then((res) => {
        let slice = res[0];
        let resource = res[1];
        if(!(slice)) throw new Error(`failed to find slice ${argv.sliceId}, please try again`)
        if(!(resource)) throw new Error(`failed to find resource ${argv.resourceUuid}, please try again`)
        
        slice.resources.push(resource.uuid);
        return db.sliceDao().update({sliceId: slice.sliceId}, slice, null);
    }).then((slice) => {
        console.log(`resource ${argv.resourceUuid} successfully added to slice ${argv.sliceId}`);
    }).catch((err) => {
        console.err(err);
    });
}