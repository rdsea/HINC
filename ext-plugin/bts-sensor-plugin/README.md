# BTS Sensor Adaptor

This adapter interfaces to generic sensor providers which can activate sensors to provide data via MQTT.

The generic sensor provider is based on  (https://github.com/rdsea/IoTCloudSamples/tree/master/IoTProviders/bts-sensor-provider)

The provider offers REST API for controlling sensors.

## Configuration

configuration settings are located in `config.js`. It includes configuration for rsiHub Local Management Service and the endpoint of the provider.

* ADAPTOR_NAME : the name of this adaptor
* URI: the URI of the rsiHubLocal Management Service
* EXCHANGE: the exchange for communicating with the rsiHub Local.
* ENDPOINT: the endpoint of the provider.

## Running with Docker

If the docker build does not have config/production.json,  
the configuration file, production.json, must be mapped to the right configuration directory: /btssensoradaptor/config/production.json.

Example of running:

 docker run -v /var/temp/config:/btssensoradaptor/config/ -e "NODE_ENV=production"  btssensoradaptor npm start
