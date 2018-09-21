# Instructions

configuration settings are located in `config.js`

* ADAPTOR_NAME : name of the adaptor,
* URI : RabbitMQ connection string (e.g. amqp://guest:guest@localhost.com),
* EXCHANGE : The exchange to consume messages from the Local Management System,


Any other configuration required to interface with a given provider is left to the discretion of the developer

## Docker run

If the docker build does not have config/production.json,  
the configuration file, production.json, must be mapped to the right configuration directory: /mqttadaptor/config/production.json.

Example of running:

 $docker run -v /var/temp/config:/mqttadaptor/config/ -e "NODE_ENV=production"  mqttadaptor npm start
