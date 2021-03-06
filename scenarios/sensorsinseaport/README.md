# Overview

This document describes steps in running the  resource slice for the scenario of sensor data exchange in the seaport.
 The short video is available in youtube: (https://youtu.be/_SCrK8Q3xBs)

# rsiHub Deployment
WARNING: Currently all artifacts can only be deployed in this way on our Google Cloud Platform account due to automated configurations. For access please contact the owners of this repository.
To deploy all these services on your own deployment configuration, please read all the relevant documentation in this repository and the IoTCloudSamples repository.

in the `deployment` folder you will find the docker-compose.yml file and relevant configuration for deploying both management services (Global Management Service and Local Management Service) and resources in the resource slices.
* Management Services:
  -Global Management Service
  - Local Management Service
* Resource providers (our own providers or wrapped providers to real providers in the cloud)
  - Sensor Provider
  - Analysis/Ingestion Provider
  - AMQP Provider (wrapped provider to Cloudamqp.com)
  - MQTT Provider
  - BigQuery Provider (wrapped provider to Google BigQuery)
* Adaptors for controlling Resource providers
  - Sensor Adaptor
  - Ingestion Adaptor
  - AMQP Adaptor
  - MQTT Adaptor
  - BigQuery Adaptor

simple in the target directory run `$ docker-compose up -d` to launch management services, providers and adaptors. Make sure to run on a relatively powerful machine due to the overhead of so many components



# Resource discovery

The slice description `sensor2bigquery.json` contains resouces that can all be discovered through the global from the following http GET endpoint `http//<global_uri>/resourceproviders`. This http endpoint returns a list of available providers and the resources that can be provisioned/managed by those providers.

We assume that the user knows semantics of the parameters of each resource provider (to provision a resource).

# Deployin a slice with pizza.js slice management client:

in the repository's `slice-management-client` directory simply run `$ node pizza.js` for the autodocumented CLI.

Before all operations make sure to run the command `$ node pizza.js config -s` to set the configuration: notably the URL of the Global (where ever you deployed in the previous step)

# Check and monitoring the basic slice

## Creating the slice

in the directory `resource-slice` a sample JSON slice is specified. simply run the command

`$ node pizza.js create resource-slice/sensor2bigquery.json`

to create the slice. The slice id/name is specified in the JSON. At the end of the operation, a new file is written with an updated deployment specification.

NOTE all future operations should be executed using the updated deployment. This updated deployment specification is always available under `$ node pizza.js slice get -d [name of slice]`

## Basic Monitoring

`$ node pizza slice get [name of slice]`

`$node pizza slice logs [slice name]`

# Slice Updates

We can alter the slice by submitting a a new specification and running the command `$ node pizza.js slice update [path to file]`

For this scenario use the add the resource in `amqpResource.json` to the resources section of the provisioned slice (this is generated by pizza).
The amqp resource can also be found through the global through the api call `http://<global_url>/resourceproviders`. You should see a an amqp provider
with the available resource.

Now call `$ node pizza slice update [path to file]` and the new amqp broker should be provisioned. The update command autogenerates another
file with an updated slice specification that includes the url of the newly provisioned broker, you can see that pizza added extra metadata to the amqp broker resource you just inserted into the slice.

The analytics program has an a outgoing http endpoint that sends data to a url through a parameter `remoteDataLocation`. However, the http endpoint
will not help to publish data the recently published amqp broker.

# Interoperability search

For the purposes of this demo, the interoperability search is mocked as the actual feature is still under development.

The intended functionality is hard coded in pizza. simple run the command

```
node pizza.js query-intop input-data-format=JSON \
                output-data-format=CSV \
                input_protocol=HTTP \
                output_protocol=AMQP
```

to simulate an interperability query, the return will contain `available_artifact` which is a software artifact that reads data from http in JSON format and publishes the data in CSV format to and amqp queue. The configuration includes the broker url and amqp_queue in the parameters section.

In reality you will need to have this artifact running somewhere with the correct configuration (amqp url and queue).

A sample of this software artifact(used in the demo) can be found under https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/httpAmqpclient

# Final Update

Now that the broker url has been provisioned and returned in the previous update operation add the parameter `remoteDataLocation` with the url of the
sofware artifact discovered in the last step. Submit a slice update through pizza and start consuming the queue of the provisioned amqp broker. After and initial delay, you should be receiveing amqp messages with the sensor data.
