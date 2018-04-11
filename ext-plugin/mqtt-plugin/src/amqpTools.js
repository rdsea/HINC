const amqp = require('amqplib');
const randomstring = require('randomstring');
const messageHandler = require('./messageHandlers/handler');
const config = require('../config');

let connection = null;
let channel = null;
let queue = `bts.sensor.plugin.${randomstring.generate()}`
let exchange = config.EXCHANGE;
let routingKey = config.ADAPTOR_NAME;
let localRoutingKey = config.LOCAL_ROUTING_KEY;
let uri = config.URI;

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
            channel.assertExchange(exchange, 'direct', {durable: false}),
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

function _handleMessage(msg){
    if(msg === null) return;
    let message = JSON.parse(msg.content.toString());
    console.log(message);
    messageHandler.handle(message).then((reply) => {
        if(reply) publish(reply);
        channel.ack(msg);    
    });
}

module.exports = {
    messageTypes,
    init,
    publish,
};





