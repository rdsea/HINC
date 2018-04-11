const providerAdaptor = require('../adaptors/provider');

function handleQueryResources(msg){
    let reply = { 
        msgType: 'UPDATE_PROVIDER',
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

    return providerAdaptor.getProvider().then((provider) => {
        reply.payload = JSON.stringify(provider);
        reply.destination = msg.reply;
        return reply;
    });
}

module.exports = handleQueryResources