# rsiHub CloudAMQP Adaptor

This adaptor is  responsible for interfacing with the CloudAMQP provider to retrieve RabbitMQ instances as resources.   

# Configuration


The configuration for the adaptor is in  config.js. It includes information about
- AMQP for communicating with rsiHub Local Management Service
- Endpoint of the Provider (CloudAMQP)

To be able to use CloudAMQP we must have a service access key to CloudAMQP. This service access key is set via an environment variable

$export CLOUDAMQP_KEY=.....

The key can be obtained from cloudamqp.com.

# Message Types

To find out the appropriate message Types check the common project in this repo
