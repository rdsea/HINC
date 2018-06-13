const resourcesAdaptor = require('../adaptors/resources');
const uuid = require('uuid/v1');

function handleQueryResources(msg){
    let reply = { 
        msgType: 'UPDATE_RESOURCES',
        senderID: 'b2807e97-2361-4518-86f8-3e7eba1da328',
        receiverID: null,
        payload: '',
        timeStamp: Math.floor((new Date()).getTime()/1000),
        uuid: uuid(),
    }

    return resourcesAdaptor.getItems().then((resources) => {
        reply.payload = JSON.stringify(resources);
        reply.destination = msg.reply;
        return reply;
    });
}

module.exports = handleQueryResources