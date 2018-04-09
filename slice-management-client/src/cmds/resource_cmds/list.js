const amqpTools = require('../../amqpTools');

exports.command = 'list [options]'
exports.desc = 'list available resources'
exports.builder = {
    limit:{
        alias: 'l',
        describe: 'limit the number of results',
        type: 'number',
        demandOption: false
    }
}

exports.handler = function (argv) {
    let payload = argv.limit ? { limit: argv.limit } : '';
    let query = amqpTools.buildMessage('FETCH_RESOURCES', payload);

    return amqpTools.init().then(() => {
        return amqpTools.sendMessage(query);
    }).then(() => {
        return amqpTools.getMessage();
    }).then((msg) => {
        msg = JSON.parse(msg.content.toString());
        if(msg.msgType !== 'DELIVER_RESOURCES')
            throw new Error(`unexpected message reply received, expected DELIVER_RESOURCES but got ${msg.msgType}`);
        let resources = JSON.parse(msg.payload);
        resources.forEach((resource, count) => {
            console.log(JSON.stringify(resource, null, 2));
            console.log('\n================================================\n')
        });    
        console.log(`retrieved ${resources.length} resources`)
    }).catch((err) => {
        console.err(err);
    }).finally(() => amqpTools.close());
}