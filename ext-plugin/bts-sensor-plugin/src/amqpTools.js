const amqp = require('amqplib');
const randomstring = require('randomstring');
const messageHandler = require('./messageHandlers/handler');
//const config = require('../config');
var config = require('config');
var sensoradaptor_config = config.get('sensoradaptor');

let connection = null;
let channel = null;
let queue = sensoradaptor_config.ADAPTOR_NAME;
let exchange = sensoradaptor_config.EXCHANGE;
let routingKey = sensoradaptor_config.ADAPTOR_NAME;
let localRoutingKey = sensoradaptor_config.LOCAL_ROUTING_KEY;
let uri = sensoradaptor_config.URI;

const messageTypes = {
    QUERY_RESOURCES: 'QUERY_RESOURCES',
    QUERY_PROVIDER: 'QUERY_PROVIDER',
    QUERY_RESOURCES_REPLY: 'QUERY_RESOURCES_REPLY',
    QUERY_PROVIDER_REPLY: 'QUERY_PROVIDER_REPLY',
    SEND_CONTROL: 'SEND_CONTROL',
    CONTROL_RESULT: 'CONTROL_RESULT',
}

function init(){
    console.log(`connecting to amqp broker at ${uri}`);
    return amqp.connect(uri).then((conn) => {
        console.log(`successfully conencted to ${uri}`);
        connection = conn;
        console.log('creating new channel');
        return connection.createChannel();
    }).then((ch) => {
        channel = ch;
        let setup = [
            channel.assertQueue(queue),
            channel.assertExchange(exchange, 'fanout'),
            channel.bindQueue(queue, exchange, routingKey),
            channel.consume(queue, _handleMessage),
        ];

        return Promise.all(setup);
    }).then(() => {
        return register(routingKey);
    });
}

function register(adaptorName){
    let payload = JSON.stringify({
        adaptorName,
    });

    let msg = {
        msgType: 'REGISTER_ADAPTOR',
        senderID: adaptorName,
        receiverID: null,
        payload: payload,
        timeStamp: Math.floor((new Date()).getMilliseconds()/1000),
        uuid: '',
    }

    publish(msg, exchange, "");
}

function publish(msg, exchange, routingKey, replyTo){
    console.log(msg)
    channel.publish(exchange, routingKey, new Buffer(JSON.stringify(msg)));
}

function sendToQueue(msg, queue, correlation){
    console.log(`sending msg to queue ${queue} with correlation ${correlation}`);
    console.log(msg);
    channel.sendToQueue(queue, new Buffer(JSON.stringify(msg)), {correlationId: correlation});
}

function _handleMessage(msg){
    console.log(JSON.stringify(msg.properties, null, 2));
    if(msg === null) return;
    let message = JSON.parse(msg.content.toString());
    console.log(JSON.stringify(message, null, 2));
    messageHandler.handle(message).then((reply) => {
        if(msg.properties.replyTo){
            sendToQueue(reply, msg.properties.replyTo, msg.properties.correlationId);
        }
        channel.ack(msg);
    });
}

module.exports = {
    messageTypes,
    init,
    publish,
};
