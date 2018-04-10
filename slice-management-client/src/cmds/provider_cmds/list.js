const amqpTools = require('../../amqpTools');
const db = require('../../data/db');

exports.command = 'list'
exports.desc = 'list available resource providers'
exports.builder = {
    limit:{
        alias: 'l',
        describe: 'limit the number of results',
        type: 'number'
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
    let query = amqpTools.buildMessage('FETCH_PROVIDERS', payload);

    if(!(argv.refresh)){
        return db.providerDao().find({}).then((resources) => {
            _displayProviders(resources);
        });
    }else{
        return amqpTools.init().then(() => {
            return amqpTools.sendMessage(query);
        }).then(() => {
            return amqpTools.getMessage();
        }).then((msg) => {
            msg = JSON.parse(msg.content.toString());
            if(msg.msgType !== 'DELIVER_PROVIDERS')
                throw new Error(`unexpected message reply received, expected DELIVER_PROVIDERS but got ${msg.msgType}`);
            let providers = JSON.parse(msg.payload);
            return _refreshProviders(providers)
            console.log(`retrieved ${providers.length} providers`)
        }).then((providers) => {
            _displayProviders(providers)
        }).catch((err) => {
            console.err(err);
        }).finally(() => amqpTools.close());
    }

    
}

function _refreshProviders(providers){
    return db.providerDao().remove({}, {multi: true}).then(() => {
        let promises = [];
        providers.forEach((provider) => {
        promises.push(db.providerDao().insert(provider));
    });
        return Promise.all(promises);
    }).then(() => {
        return providers;
    });    
}



function _displayProviders(providers){
    providers.forEach((provider, count) => {
        console.log(JSON.stringify(provider, null, 2));
        console.log('\n================================================\n')
    });    
    console.log(`retrieved ${providers.length} providers`)
}