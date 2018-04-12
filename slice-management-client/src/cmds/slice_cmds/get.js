const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');

exports.command = 'get <sliceId>'
exports.desc = 'get details about slice <sliceId>'
exports.builder = {}

exports.handler = function (argv) {
    let slice = null;
    db.sliceDao().findOne({sliceId: argv.sliceId}).then((s) => {
        if(!(s)) throw new Error(`could not find slice ${argv.sliceId}, please try again`);

        slice = s;
        let resourceQueries = []
        slice.resources.forEach((uuid) => {
            resourceQueries.push(db.resourceDao().findOne({uuid}));
        });
        return Promise.all(resourceQueries);
    }).then((resources) => {
        let table = new Table({
            head: ['resource uuid', 'resource type', 'resource name'],
        });

        resources.forEach((resource) => {
            table.push([resource.uuid, resource.resourceType, resource.name]);
        });

        console.info(`slice ${argv.sliceId} created ${moment.unix(slice.createdAt).fromNow()}`);
        console.log(table.toString());
    }).catch((err) => {
        console.err(err);
    })
}