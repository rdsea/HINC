const resourcesAdaptor = require('../adaptors/resources');
const uuid = require('uuid/v1');
const configModule = require('config');
const config = configModule.get('dockerizedservice');
function handleQueryResources(msg){
    let reply = {
        msgType: 'UPDATE_RESOURCES',
        senderID: config.ADAPTOR_UUID,
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
