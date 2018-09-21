const deleteAdaptor = require('../controls/delete');
const configModule = require('config');
const config = configModule.get('cloudmqtt');

function handleDelete(msg){
    let reply = { 
        msgType: 'CONTROL_RESULT',
        senderID: config.ADAPTOR_NAME,
        receiverID: null,
        payload: '',
        timeStamp: Math.floor((new Date()).getMilliseconds()/1000),
        uuid: '',
    }

    let resource = JSON.parse(msg.payload);
    return deleteAdaptor.deleteResource(resource).then((controlResult) => {
        reply.payload = JSON.stringify(controlResult);

        return reply;
    });
}

module.exports = handleDelete;