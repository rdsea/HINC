#Cloud MQTT Adaptor

We consider CloudMQTT as a resource provider. This adaptor is used to interface to the CloudMQTT to ask for resources.

For the testing purpose, we will use only free plans.

## Configuration

The configuration is defined in config/production.json.
This configuration will be enabled by using the environment variable "NODE_ENV=production".  The configuration file includes information about

- AMQP for communicating with rsiHub Local Management Service
- Endpoint of the Provider (CloudMQTT)

To be able to use CloudMQTT we must have a service access key to CloudMQTT. This service access key is set via an environment variable

$export CLOUDMQTT_KEY=.....

The key can be obtained from cloudqmtt.

## Running with Docker

If the docker build does not have config/production.json,  
the configuration file, production.json, must be mapped to the right configuration directory: /cloudmqttadaptor/config/production.json.

Example of running:

 docker run -v /var/temp/config:/cloudmqttadaptor/config/ -e "NODE_ENV=production" -e "CLOUDMQTT_KEY=aaa" cloudmqttadaptor npm start
