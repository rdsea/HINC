const provisionAdaptor = require('../controls/provison');

function handleProvision(msg){
    let reply = { 
        msgType: 'CONTROL_RESULT',
        senderID: 'b2807e97-2361-4518-86f8-3e7eba1da328',
        receiverID: null,
        payload: '',
        timeStamp: Math.floor((new Date()).getMilliseconds()/1000),
        uuid: '732330',
        destination: { 
            exchange: 'test.adaptors', 
            routingKey: 'testy' 
        },
        reply: { 
            exchange: 'test.adaptors', 
            routingKey: 'test.local' 
        },
    }

    let resource = JSON.parse(msg.payload);
    return provisionAdaptor.provision(resource).then((controlResult) => {
        reply.payload = JSON.stringify(controlResult);
        reply.destination = msg.reply;

        return reply;
    });
}

module.exports = handleProvision;