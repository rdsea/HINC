const amqpTools = require('../../amqpTools');

exports.command = 'list'
exports.desc = 'list available resource providers'
exports.builder = {
    limit:{
        alias: 'l',
        describe: 'limit the number of results',
        type: 'number'
    }
}

exports.handler = function (argv) {
    let payload = argv.limit ? { limit: argv.limit } : '';
    let query = amqpTools.buildMessage('FETCH_PROVIDERS', payload);

    return amqpTools.init().then(() => {
        return amqpTools.sendMessage(query);
    }).then(() => {
        return amqpTools.getMessage();
    }).then((msg) => {
        msg = JSON.parse(msg.content.toString());
        if(msg.msgType !== 'DELIVER_PROVIDERS')
            throw new Error(`unexpected message reply received, expected DELIVER_PROVIDERS but got ${msg.msgType}`);
        let providers = JSON.parse(msg.payload);
        providers.forEach((resource, count) => {
            console.log(JSON.stringify(resource, null, 2));
            console.log('\n================================================\n')
        });    

        console.log(`retrieved ${providers.length} providers`)
    }).catch((err) => {
        console.err(err);
    }).finally(() => amqpTools.close());
}