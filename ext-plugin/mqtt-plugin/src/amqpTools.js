const amqp = require('amqplib');
const randomstring = require('randomstring');
const messageHandler = require('./messageHandlers/handler');
const config = require('../config');

let connection = null;
let channel = null;
let queue = config.ADAPTOR_NAME
let exchange = config.EXCHANGE;
let routingKey = config.ADAPTOR_NAME;
let localRoutingKey = config.LOCAL_ROUTING_KEY;
let uri = config.URI;

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
        destination: { 
            exchange: exchange, 
            routingKey: localRoutingKey 
        },
        reply: { 
            exchange: exchange, 
            routingKey: routingKey 
        },
    }

    publish(msg);
}

function publish(msg){
    console.log(msg)
    channel.publish(msg.destination.exchange, msg.destination.routingKey, new Buffer(JSON.stringify(msg)));
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
    init,
    publish,
};





