const deleteAdaptor = require('../controls/delete');

var noderedplugin_config = require('config');
var config = noderedplugin_config.get('noderedadaptor');
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
