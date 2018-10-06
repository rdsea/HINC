# Docker Service Adaptor

This adaptor is used to interface to [the GenericDockerProvider]()


## Configuration

The configuration is defined in config/production.json.
This configuration will be enabled by using the environment variable "NODE_ENV=production".  The configuration file includes information about

- AMQP for communicating with rsiHub Local Management Service
- Endpoint of the Provider (the GenericDockerProvider)


## Running with Docker

If the docker build does not have config/production.json,  
the configuration file, production.json, must be mapped to the right configuration directory: /dockerprovideradaptor/config/production.json.

Example of running:

 docker run -v /var/temp/config:/dockerprovideradaptor/config/ -e "NODE_ENV=production"  rdsea/dockerprovider-adaptor npm start
