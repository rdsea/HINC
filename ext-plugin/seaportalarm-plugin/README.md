# Alarm Plugin

This plug-in is used to inteface to an Alarm Provider in a seaport.
Example of the Alarm Provider is in [IoTCloudSamples/IoTProviders/alarm-client-provider]( https://github.com/rdsea/IoTCloudSamples/tree/master/IoTProviders/alarm-client-provider)

Through this plug-in, one can ask for a resource to access the alarm data in the seaport provided by the corresponding provider.

## Configuration of the plugin

configuration settings are located in `config.js`

* ADAPTOR_NAME : name of the adaptor,
* URI : RabbitMQ connection string (e.g. amqp://guest:guest@localhost.com) used to communicate with the rsiHubLocalService
* EXCHANGE : The exchange to consume messages from rsiHubLocalService
* ENDPOINT: the URI of the AlarmProvider

Any other configuration required to interface with a given provider is left to the discretion of the developer

## Run

The rsiHubLocalService must be run and the AlarmProvider is also running, before running the plug-in.
