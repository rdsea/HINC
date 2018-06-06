const provisionAdaptor = require('../controls/provision');
const deleteAdaptor = require('../controls/delete');
const config = require('../../config');

function handleProvision(msg){
    let reply = { 
        msgType: 'CONTROL_RESULT',
        senderID: config.ADAPTOR_NAME,
        receiverID: null,
        payload: '',
        timeStamp: Math.floor((new Date()).getMilliseconds()/1000),
        uuid: '732330',
    }

    let resource = JSON.parse(msg.payload);
    return deleteAdaptor.deleteResource(resource).then(() => {
        return provisionAdaptor.provision(resource);
    }).then((controlResult) => {
        reply.payload = JSON.stringify(controlResult);
        reply.destination = msg.reply;

        return reply;
    }); 
}

module.exports = handleProvision;