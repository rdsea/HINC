const amqp = require('amqplib');
const randomstring = require('randomstring');
const config = require('./config');
const uuid = require('uuid/v1');

let uri = config.uri
let exchange = config.exchange;

let connection = null;
let channel = null;
let queue = config.routingKey
let routingKey = config.routingKey;

function init(){
    console.debug('initializing amqp connection');
    console.debug(`connecting to amqp broker at ${uri}`);
    return amqp.connect(uri).then((conn) => {
        console.debug(`successfully conencted to ${uri}`);
        connection = conn;
        console.debug('creating new channel');
        return connection.createChannel();
    }).then((ch) => {
        channel = ch;
        let setup = [
            channel.assertQueue(queue),
            channel.assertExchange(exchange, 'direct', {durable: false}),
            channel.bindQueue(queue, exchange, routingKey),
        ];

        return Promise.all(setup);
    }).catch((err) => {
        console.err(`failed to connect to amqp exchange: ${exchange} at ${uri} `);
        throw err;
    });
}

function sendMessage(msg){
    console.debug('publishing message');
    console.debug(msg)
    return channel.publish(msg.destination.exchange, msg.destination.routingKey, new Buffer(JSON.stringify(msg)));
}

function getMessage(timeout=10000){
    return _pollMessage(timeout)
}

function _pollMessage(timeout){
    let start = (new Date()).getTime();
    console.debug(`start polling for message, timeout in ${timeout} milliseconds`);
    let whileNotTimeout = () => {
        return channel.get(queue, {noAck: true}).then(function(res) {
            let current = (new Date().getTime());
            if(current-start>timeout && timeout > 0) throw new Error(`Timeout of ${timeout} milliseconds exceeded for expected message`);

            if (res === false) {
                 // run the operation again
                 return whileNotTimeout();
            } else {
                console.debug('message received');  
                return res;
            }
        });
    }

    return whileNotTimeout();
}

function buildMessage(msgType, payload){
    return  { 
        msgType: msgType,
        senderID: '',
        receiverID: null,
        payload: payload,
        timeStamp: Math.floor((new Date()).getTime()/1000),
        uuid: uuid(),
        destination: { 
            exchange: config.exchange, 
            routingKey: config.localRoutingKey, 
        },
        reply: { 
            exchange: config.exchange, 
            routingKey: config.routingKey, 
        },
    }
}

function close(){
    console.debug('closing amqp connection');
    channel.close();
    connection.close();
}

module.exports = {
    init,
    sendMessage,
    getMessage,
    buildMessage,
    close,
};




