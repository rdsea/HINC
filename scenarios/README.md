# Interoperability Scenarios

This directory includes a set of scenarios in which one can
create resource slices for IoT Interoperability problem.

The overall scenario is about the Interoperability of data and services for a seaport (e.g., in our example, Valencia seaport in the Inter-IoT project).

In the following we describe scenarios, whereas technical setup details will be given in sub-directories.

## Software assumption in seaport


### IoT Cameras

In our scenarios, in the seaport, there are many cameras which provide real-time data (and historical data). Each camera has metadata about the location, video data, etc. Cameras are a service unit, a resource, whose data can be pushed by the camera or a service provider or pulled by consumer.
There is a CameraProvider for the seaport which allows consumers to search and request resources. The provider manages cameras:

- Currently  there is no access to cameras in the real seaport, we will use camera in public street for this.  We use the code in https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/IoTCameraDataProvider for cameras.

### IoT sensors:
In the seaport there are many sensors which provide information about:

* EmissionCabins
* MeteoStations
* WeatherMeasurements
* EmissionMeasurements
* SoundMeasurements
* SoundMeters

We consider these types of data from various sensors and IoT providers. We use the code in https://github.com/rdsea/IoTCloudSamples/tree/master/IoTProviders/bts-sensor
to emulate real sensors and data providers by taking existing dataset from Valencia ports and replay them. We can also use other datasets to demonstrate other sources of data, e.g., electricity current, alarms. However, they can be only used for the purpose of demonstrating data request as the data cannot be related to the port scenario.


### Network Functions

 In the seaport there is a Network Function provider which offers firewall functions that can control the traffic in/out the seaport. We will emulate this by assuming that all the seaport infrastructure is running as a google cloud virtual infrastructure. Our network function provider leverages google firewall features. We use the code in https://github.com/rdsea/IoTCloudSamples/tree/master/NetworkfunctionsUnits/SimpleFirewallController

### Edge computing brokers
 In the seaport there is a provider which can offer brokers on demand. The broker we use in our example is MQTT. If a consumer needs, the provider will provide an instance of the broker for the consumers. We use https://github.com/rdsea/IoTCloudSamples/tree/master/IoTProviders/mosquitt-mqtt-provider for testing.

### Edge computing workflow
In the seaport there is a provider which can offer a data processing workflow engine that one can use for its work. The data processing workflow we use is NODE-RED. We use the code https://github.com/rdsea/IoTCloudSamples/tree/master/InterOpProviders/nodered-datatransformer-provider for this.

### Cloud services:
There are many cloud services available outside the port. We use BigQuery, virtual machines, Google Storage, etc. Furthermore the computing brokers and computing workflows (like in the edge situation within the seaport) can also be provided as cloud services.


## Accessing Video Data in seaport

### Interoperability features

To demonstrate protocol interoperability at runtime with dynamic resource slice provisioning.

### Resource slice:

- A consumer within the seaport requests the Provider some cameras and obtains the video camera data by pulling the data itself. (using HTTP)
- An other consumer outside the seaport does the same.

Here we have a slice includes cameras as resources and two consumers

### Resource slice reconfiguration

- Now an emergency event happens, the consumer outside the seaport asks the provider to reconfigure resources and push the data to an outside cloud service. For the example, the data has to be pushed into Google Storage. The second option is the data has to be pushed into Kafka.

Here the slice has to be reconfigured with:

- The network function must allow "pushing data" to Google Storage
- The camera provider must be reconfigured.


### Accessing Sensor Data in seaport

### Interoperability

Middleware interoperability, protocol interoperability and data interoperability with different solutions.

### Resource slice

A consumer in the seaport wants to access sensor data in the seaport with the condition of nosharing middleware. The resource slice will be created, including

- sensors: for sensor data. We assume that the data is in the form of CSV (raw)
- MQTT: for brokers

### Resource Slice reconfiguration

------------------
#### First situation:

the consumer wants to process data from the broker using a separate workflow engine within the seaport. The slice is reconfigured with a new Node-RED instance and the consumer adds a workflow into NODE-RED.

------------------
#### Second situation:
Another consumer wants to access the sensor data from the broker but finds that the data is in CSV, thus the consumer wants to deploy a resource to transform CSV data to JSON. Two possible solutions:

- a new component is deployed that takes data from MQTT and transforms the CSV to the JSON. This component is based on (https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/csvToJson)

- a new NODE-RED instance is created and a workflow for data transformation is pushed into the instance.

The two cases achieve the same goal but have very different techniques.

------------------
#### Third situation

Similar to the second situation but the consumer is outside the seaport. Thus, cloud services are used and Network Function is also enabled.

## Accessing Sensors and Cameras

### Interoperability

Middleware interoperability, protocol interoperability, data interoperability

### Resource Slice
An accident happens at a terminal, a consumer (e.g., seaport management) needs to access all sensors and cameras centered the terminal. The data will be sent to different consumers

We first create a slice including sensors and cameras.

### Slice reconfiguration

When a new consumer (e.g. emergency) needs a new data (e.g., images) to be pushed into the system of the consumer. We will do:
- Identify if a broker, etc needed and provisioned.
- Identify if a data transformation is needed then we do
- Identify if a protocol is needed to reconfigure then we do
- Identify if a network firewall operation is needed then we do

Here we make assumption that we have the information about the services that the consumer wants (e.g, JSON/CSV for data, MQTT/Kafka for Broker, Storage, etc)

## Controlling

### Interoperability
Middleware, protocol, data.

### Resource Slice
An emergency happens. The consumer within the seaport (e.g., seaport manager) runs a slice (sensors, broker, workflow) and determine to send a command to another consumer (e.g., crane controller or vessels).

Initially the slice is just about sensors, broker, workflow.

### Resource Slice Configuration

The consumer decides to send commands to other consumers (called receivers). The consumer decides to add  a new component that takes the result of an analytics (e.g., from the queue) then ingest the data into a component that produces commands for receivers.

## Data Exchange and Control in Emergency situation

In this scenario, we assume there are alarms occurring in a seaport. The alarms are propagated through an MQTT broker. Usually, there are some analytics applications listening the alarms queues to react to the alarms.

One of such alarms analytics programs finds alarms related to terminals in the port. It queries a PortControlService (PCS) to obtain the list of vessels approaching the port. Based on the information about the vessels and the service providers of the vessels, the alarms analytics program creates new brokers as resources or connects to existing communication means of the vessel providers to share the information about the situations. The program can also send requests to ask vessels to stop or change the plan to arrive terminals.

Similarly, another analytics program can also inform other relevant objects around the terminals (e.g., by using geohash to query cranes and trucks) and requests them to stop or change the plan.

Another analytics program can request camera providers (for cameras close to the terminal, using geohash)  to provide videos to separate channels that can be accessed by polices and other relevant third parties.

We have:

* alarmgenerator to generate samples of alarms
* mqtt broker for alarms
* python/nodejs analytics programs
* A PCS emulating the port control. the PCS has APIs for querying vessels and for updating vessels positions. PCS has the back-end database as mongodb.
* A set of vessel emulators (python/nodejs) emulate  the movement of vessels. An vessels emulator subscribes information from its providers via a queue.
* A set of vessel service providers. Each providers accept a different format of data (JSON/CSV with different structures) and use different protocols (MQTT, AMQP and Webservice).
* Cranes and trucks are similar vessels with cranes/trucks  and their providers
* Vessels/trucks/cranes/cameras have their GPS positions so that geohash can be used to query them.

Note that to demonstrate the diversity of providers: we have at different providers for each category: vessel, truck and crane. 

### Resource slices

Various resource slices can be created during the emergency situations. Network functions can also be controlled.

### Interoperability

Since vessel/truck/crane providers accept different forms of data and protocols, we need to search suitable interoperability bridge and instantiate them accordingly.
