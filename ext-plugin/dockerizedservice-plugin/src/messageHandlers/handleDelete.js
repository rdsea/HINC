const deleteAdaptor = require('../controls/delete');
const uuid = require('uuid/v1');
const configModule = require('config');
const config = configModule.get('dockerizedservice');

function handleDelete(msg){
    let reply = {
        msgType: 'CONTROL_RESULT',
        senderID: config.ADAPTOR_UUID,
        receiverID: null,
        payload: '',
        timeStamp: Math.floor((new Date()).getMilliseconds()/1000),
        uuid: uuid(),
    }

    let resource = JSON.parse(msg.payload);
    return deleteAdaptor.deleteResource(resource).then((controlResult) => {
        reply.payload = JSON.stringify(controlResult);

        return reply;
    });
}

module.exports = handleDelete;
