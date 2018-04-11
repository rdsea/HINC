const amqpTools = require('../../amqpTools');
const fs = require('fs');
const path = require('path');

exports.command = 'control [options]'
exports.desc = 'sends a control specified in file'
exports.builder = {
    file:{
        alias: 'f',
        describe: 'path to the json file',
        demandOption: true,
        type: 'string',
    },
}

exports.handler = function (argv) {
    let control = fs.readFileSync(path.join(process.cwd(), argv.file));
    let controlMsg = amqpTools.buildMessage('CONTROL', control.toString());
    
    return amqpTools.init().then(() => {
        return amqpTools.sendMessage(controlMsg);
    }).then(() => {
        return amqpTools.getMessage(-1);
    }).then((msg) => {
        msg = JSON.parse(msg.content.toString());
        if(msg.msgType !== 'CONTROL_RESULT')
            throw new Error(`unexpected message reply received, expected CONTROL_RESULT but got ${msg.msgType}`);
        let controlResult = JSON.parse(msg.payload);
        console.log(JSON.stringify(controlResult, null, 2));  
    }).catch((err) => {
        console.err(err);
    }).finally(() => amqpTools.close());
}
