const deleteAdaptor = require('../controls/delete');

var mqttplugin_config = require('config');
var config = mqttplugin_config.get('mqttadaptor');

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
