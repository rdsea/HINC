# Simple alarm scenario

## Test setup
This shows a simple alarm scenario setup in some machines to understand resources required for this scenario. Note the following things

- IoTCloudSamples is github.com/rdsea/IoTCloudSamples

Make sure you have an MQTT setup for messages:

* You can have one or many MQTT instances. For simple test, one instance is enough but make sure that all entities are used different topics.

### Run one instance of the seaport port control service:

* code: IoTCloudSamples/IoTCloudUnits/port-control-service
* Configure the information about port, alarm broker topics, etc.
* run it with $npm start

### Run one or more vessels. A vessel is emulated by code listening brokers.

* Code: IoTCloudUnits/portVessel

### Run the port alarmService

* Code: IoTCloudUnits/portAlarmService
* Make sure the configuration of alarm broker, etc. correct
* run the alarmService: $npm start

### Run alarm sensor simulator

* Code: IoTCloudUnits/portalarmsensor


Now you should see the vessel receives the information from the alarm service.

## Interoperability question

There are some situations:

* if we have different vessel companies and vessels use different data format and protocol, how interoperability bridges can be used?
- if we have different analytics producing different alarms, what would be the best way to support
- if we have other objects then vessels, the objects use different protocols and data formats, what should we do.
