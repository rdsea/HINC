const logsAdaptor = require('../controls/logs');

function handleGetLogs(msg){
    let reply = { 
        msgType: 'CONTROL_RESULT',
        senderID: 'b2807e97-2361-4518-86f8-3e7eba1da328',
        receiverID: null,
        payload: '',
        timeStamp: Math.floor((new Date()).getMilliseconds()/1000),
        uuid: '732330',
    }

    let resource = JSON.parse(msg.payload);
    return logsAdaptor.getLogs(resource).then((controlResult) => {
        reply.payload = JSON.stringify(controlResult);
        reply.destination = msg.reply;

        return reply;
    });
}

module.exports = handleGetLogs;