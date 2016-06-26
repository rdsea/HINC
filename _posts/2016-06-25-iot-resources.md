---
layout: page
title: "IoT resources"
category: tut
date: 2016-06-25 10:37:02
order: 1
---

The followings tutorials will shows you how to set up the IoT resources, which are part of other HINC tutorials. These tutorials can also help you in setup your own IoT testbed. You will find followings:

- [Emulated sensors](#emulated-sensor)
- [Dummy provider](#dummy-provider)
- [OpenHAB](#openhab)
- [IoTivity](#iotivity)

**Note**: The folowing guides use binary artifacts. You can also check and build from source codes, which will be mentioned in each part.


## Emulated sensor
Emulated sensor is a small program that reads input data from a CSV file line-by-line, and send each line to a particular output every interval. Next steps you will configure a sensor to read a temperature data and send to a MQTT broker. The source code of the sensor can be found on [Github](https://github.com/EllaPham/TEIT)

First, you need a working directory for the sensor, please create it:

```sh
$ mkdir /tmp/sensor123
$ cd /tmp/sensor123
```

Second, you download the latest binary version and the data files for a temperature sensor:

```sh
$ wget https://github.com/EllaPham/TEIT/raw/artifacts/EmulatedSensor-1.0-SNAPSHOT.jar
$ wget https://github.com/EllaPham/TEIT/raw/artifacts/temp123.tar.gz
$ tar -xvzf temp123.tar.gz
$ rm temp123.tar.gz
```

You will see following files in the download folder (which can be editted freely based on your need)

- EmulatedSensor-1.0-SNAPSHOT.jar: The binary to run the sensor
- sensor.conf: The configuration file. You can change some parameters like sensor ID, sending rate, input and output.
- temp123.csv: An example of temperature data in a room.
- temp123.meta: Some metadata about the sensor itself.

Third, you run the sensor. It will start to read data from the `temp123.csv` and send to `iot.eclipse.org` (as configured in sensor.conf).

```sh
$ java -jar EmulatedSensor-1.0-SNAPSHOT.jar
```

You would see the ouput like this:

```sh
11:23:31 DEBUG                 Main:26 - Starting sensor...
11:23:31 DEBUG                 Main:36 - Input : teit.sensor.CSVFile.CSVDataAdaptor
11:23:31 DEBUG                 Main:37 - Output: teit.sensor.MQTT.MQTTOutput
11:23:31 DEBUG       CSVDataAdaptor:37 - CSV File:temp123.csv
11:23:31 DEBUG       CSVDataAdaptor:42 - Keyset:[timestamp, temperature]
11:23:31 DEBUG           MQTTOutput:32 - MQTTAdaptor -- broker: tcp://iot.eclipse.org:1883 -- topic: topic-sensor123
11:23:35 DEBUG                 Main:57 - Starting to read data .... 

11:23:35 DEBUG       CSVDataAdaptor:64 - CSV read one line: 2012-10-23 19:56:00,20.8
11:23:35 DEBUG                 Main:60 - Data item is read:{temperature=20.8, timestamp=2012-10-23 19:56:00, sensorid=temp123}
11:23:36 DEBUG           MQTTOutput:59 - Published: {temperature=20.8, timestamp=2012-10-23 19:56:00, sensorid=temp123}
11:23:36 DEBUG                 Main:78 - Sleeping for 5000 mili-seconds ...

11:23:41 DEBUG       CSVDataAdaptor:64 - CSV read one line: 2012-10-23 19:57:00,20.8037037037
11:23:41 DEBUG                 Main:60 - Data item is read:{temperature=20.8037037037, timestamp=2012-10-23 19:57:00, sensorid=temp123}
11:23:41 DEBUG           MQTTOutput:59 - Published: {temperature=20.8037037037, timestamp=2012-10-23 19:57:00, sensorid=temp123}
11:23:41 DEBUG                 Main:78 - Sleeping for 5000 mili-seconds ...
```

Now, you want to see if the sensor is sending data. You open another terminal to run the MQTT subscriber

```sh
$ wget https://github.com/EllaPham/TEIT/raw/artifacts/MQTTSubscriber-1.0-SNAPSHOT.jar
$ java -jar target/MQTTSubscriber-1.0-SNAPSHOT.jar tcp://iot.eclipse.org:1883 topic-sensor123
```

The output should be like this:

```sh
Connected to the MQTT broker: tcp://iot.eclipse.org:1883
Subscribed the topic: topic-sensor123.
Listening to the incoming data... 

{temperature=20.8192307692, timestamp=2012-10-23 20:01:00, sensorid=temp123}
{temperature=20.8698113208, timestamp=2012-10-23 20:02:00, sensorid=temp123}
```


## Dummy provider

The Dummy provider emulates a large number of devices in order to experiement the performance of the system. Dummy provider do not produce any data, but the information of many devices. Next step you will deploy a Dummy provider to generate 500 sensor instances. Source code is on [Github](https://github.com/SINCConcept/HINC/tree/master/ext-pom/DummyProvider).

To download and run the provider is easy, as below

```sh
$ wget https://github.com/SINCConcept/HINC/blob/artifacts/plugins/DummyProvider-1.0.jar
$ java -jar DummyProvider-1.0.jar 8888 500
```

The java command takes two parameters: the running port and the number of sensor to be generated. After the provider running, you can call to its RESTful service for the information:

```sh
$ curl http://localhost:8888/datapoints
```

## OpenHAB
OpennHAB is a framework for SmartHome, supports multiple types of devices, works at low level resource model.
On the [OpenHAB website](http://www.openhab.org/getting-started/downloads.html), download the **Runtime core** and the **Demo setup** and follow the guideline on its official website.

## IoTivity

 -- to be written --
