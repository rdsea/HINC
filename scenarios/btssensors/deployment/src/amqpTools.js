const amqp = require('amqplib');

let connection = null;
let channel = null;


// an array of exchanges and the queues bound to them
function setupBroker(exchanges, uri){
    console.log(`connecting to amqp broker at ${uri}`);
    return amqp.connect(uri).then((conn) => {
        console.log(`successfully conencted to ${uri}`);
        connection = conn;
        console.log('creating new channel');
        return connection.createChannel();
    }).then((ch) => {
        channel = ch;
        let setup = [];

        for(label in exchanges){
            let exchange = label;
            let exchangeType = exchanges[label].type;
            let queues = exchanges[label].queues;
            let durable = exchanges[label].durable;
            
            let exchangeSetup = [
                channel.assertExchange(exchange, exchangeType)
            ];

            queues.forEach((queue) => {
                exchangeSetup.push(
                    channel.assertQueue(queue, {durable: false}),
                    channel.bindQueue(queue, exchange, "")
                );
            });
            setup = setup.concat(exchangeSetup);

        }

        return Promise.all(setup);
    }).then(() => {
        console.log("successfully setup broker");
        connection.close();
    }).catch((err) => {
        console.error("failed to setup broker");
        console.error(err);
    })
}

module.exports = setupBroker;