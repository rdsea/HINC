const amqpTools = require('../../amqpTools');
const db = require('../../data/db');

exports.command = 'list [options]'
exports.desc = 'list available resources'
exports.builder = {
    limit:{
        alias: 'l',
        describe: 'limit the number of results',
        type: 'number',
        demandOption: false
    },
    refresh:{
        alias: 'r',
        describe: 'refresh data from datasource',
        type: 'boolean',
        demandOption: false
    }
}

exports.handler = function (argv) {
    let payload = argv.limit ? { limit: argv.limit } : '';
    let query = amqpTools.buildMessage('FETCH_RESOURCES', payload);

    if(!(argv.refresh)){
        return db.resourceDao().find({}).then((resources) => {
            _displayResources(resources);
        });
    }else{
        return amqpTools.init().then(() => {
            return amqpTools.sendMessage(query);
        }).then(() => {
            return amqpTools.getMessage();
        }).then((msg) => {
            msg = JSON.parse(msg.content.toString());
            if(msg.msgType !== 'DELIVER_RESOURCES')
                throw new Error(`unexpected message reply received, expected DELIVER_RESOURCES but got ${msg.msgType}`);
    
            let resources = JSON.parse(msg.payload);
            return _refreshResources(resources);            
        }).then((resources) => {
            _displayResources(resources);
        }).catch((err) => {
            console.err(err);
        }).finally(() => amqpTools.close());
    }
}

function _refreshResources(resources){
    return db.resourceDao().remove({}, {multi: true}).then(() => {
        let promises = [];
    resources.forEach((resource) => {
        promises.push(db.resourceDao().insert(resource));
    });
        return Promise.all(promises);
    }).then(() => {
        return resources;
    });    
}



function _displayResources(resources){
    resources.forEach((resource, count) => {
        console.log(JSON.stringify(resource, null, 2));
        console.log('\n================================================\n')
    });    
    console.log(`retrieved ${resources.length} resources`)
}