const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');
const config = require("../../config")
const axios = require('axios');
const meshService = require("../../services/mesh/mesh")

exports.command = 'delete <sliceId>'
exports.desc = 'deletes slice <sliceId> and all its resources'
exports.builder = {}

exports.handler = function (argv) {
    let slice = null;
    db.sliceDao().findOne({sliceId: argv.sliceId}).then((s) => {
        if(!(s)) throw new Error(`could not find slice ${argv.sliceId}, please try again`);
        slice = s;
        let deleteResourcePromises = [];
        for(let label in slice.resources){
            console.debug(`deleting resource:`);
            console.debug(JSON.stringify(slice.resources[label], null, 2))
            deleteResourcePromises.push(axios.delete(`${config.uri}/controls/delete`, {data: slice.resources[label]}));
        }

        return Promise.all(deleteResourcePromises);
    }).then((res) => {
        return db.sliceDao().remove(slice, {});
    }).then(() => {
        return meshService.deleteMesh(argv.sliceId);
    }).catch((err) => {
        console.err(err);
    })
}