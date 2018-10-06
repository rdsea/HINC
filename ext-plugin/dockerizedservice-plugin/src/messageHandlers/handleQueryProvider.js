const providerAdaptor = require('../adaptors/providers');
const uuid = require('uuid/v1');
const configModule = require('config');
const config = configModule.get('dockerizedservice');
function handleQueryResources(msg){
    let reply = {
        msgType: 'UPDATE_PROVIDER',
        senderID: config.ADAPTOR_UUID,
        receiverID: null,
        payload: '',
        timeStamp: Math.floor((new Date()).getMilliseconds()/1000),
        uuid: uuid()
    }

    return providerAdaptor.getProvider().then((provider) => {
        reply.payload = JSON.stringify(provider);
        reply.destination = msg.reply;
        return reply;
    });
}

module.exports = handleQueryResources
