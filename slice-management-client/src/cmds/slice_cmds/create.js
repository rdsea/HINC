const db = require('../../data/db');
const moment = require('moment');

exports.command = 'create <sliceId>'
exports.desc = 'creates a slice identified by <sliceId>'
exports.builder = {}
exports.handler = function (argv) {
    console.log(`creating slice ${argv.sliceId}`)   
    return db.sliceDao().insert({
        sliceId: argv.sliceId,
        createdAt: moment().unix(),
        resouces: [],
    }).then((slice) => {
        console.log(`successfully created slice ${slice.sliceId}`);
    })  
}