#Cloud MQTT Adaptor

This adaptor is used to interface to cloudmqtt.com

## Configuration

The configuration is defined in config/production.json.
This configuration will be enabled by using the environment variable "NODE_ENV=production"

## Running with Docker

If the docker build does not have config/production.json,  
the configuration file, production.json, must be mapped to the right configuration directory: /cloudmqttadaptor/config/production.json.

Example of running:

 docker run -v /var/temp/config:/cloudmqttadaptor/config/ -e "NODE_ENV=production" -e "CLOUDMQTT_KEY=aaa" cloudmqttadaptor npm start
