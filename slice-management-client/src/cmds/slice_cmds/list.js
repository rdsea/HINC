const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');

exports.command = 'list [limit]'
exports.desc = 'list [limit] available slices'
exports.builder = {}
exports.handler = function (argv) {
    console.log(`listing ${argv.limit || 'all'} slices`);

    // TODO add more information to this output
    return db.sliceDao().find().then((slices) => {
        let table = new Table({
            head: ['slice ID', 'Created'],
        });
        slices.forEach((slice) => {
            table.push([
                slice.sliceId,
                moment.unix(slice.createdAt).fromNow(),
            ]);
        })

        console.log(table.toString());
    })
}