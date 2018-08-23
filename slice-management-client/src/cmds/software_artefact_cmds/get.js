const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');

exports.command = 'get <sliceId>'
exports.desc = 'get details about slice <sliceId>'
exports.builder = {
    detailed:{
        alias: 'd',
        describe: 'display detailed JSON slice specification',
        type: 'boolean',
        demandOption: false
    }
}

exports.handler = function (argv) {
    let slice = null;
    db.sliceDao().findOne({sliceId: argv.sliceId}).then((s) => {
        if(!(s)) throw new Error(`could not find slice ${argv.sliceId}, please try again`);

        if(argv.detailed){
            console.log(JSON.stringify(s, null, 2));
        }else{
            _displayOverview(s);
        }
        
    }).catch((err) => {
        console.err(err);
    })
}

function _displayOverview(slice){
    let table = new Table({
        head: ['resource uuid', 'resource type', 'resource name'],
    });

    for(label in slice.resources){
        let resource = slice.resources[label];
        table.push([resource.uuid || "", resource.resourceType || "", resource.name || ""]);
    }

    console.log(table.toString());
}