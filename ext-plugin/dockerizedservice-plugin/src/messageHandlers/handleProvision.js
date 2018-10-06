const provisionAdaptor = require('../controls/provision');
const uuid = require('uuid/v1');
const configModule = require('config');
const config = configModule.get('dockerizedservice');
function handleProvision(msg){
    let reply = {
        msgType: 'CONTROL_RESULT',
        senderID: config.ADAPTOR_UUID,
        receiverID: null,
        payload: '',
        timeStamp: Math.floor((new Date()).getMilliseconds()/1000),
        uuid: uuid(),
    }

    let resource = JSON.parse(msg.payload);
    return provisionAdaptor.provision(resource).then((controlResult) => {
        reply.payload = JSON.stringify(controlResult);
        reply.destination = msg.reply;

        return reply;
    });
}

module.exports = handleProvision;
