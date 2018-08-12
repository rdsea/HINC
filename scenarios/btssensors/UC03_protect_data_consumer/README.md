# BTS Monitoring System Scenario

Our scenario is based on a BTS system . This is a machine that enables wireless communication between user equipment, for example a mobile phone or a computer, and networks like the GSM network. The data is received through an antenna and is then processed and transmitted by the BTS to create a wireless connection. 

BTSs are made up of various components working together such as a power amplifier,
filter, low noise amplifier, duplexer, antenna system and power module. Additionally a BTS typically 
has multiple transceivers that allow it to serve many of the cell's different frequencies and sectors.
A BTS is typically fitted with an array of sensors, which can be build in to the BTS during the 
manufacturing phase or be a purchased add-on. 

These sensors detect the operating environment of the BTS, which can be used for various big data
analysis of cell networks. Additionally, the sensors also facilitate the equipment faults and alarms.
These sensors produce data that is sent to an IoT gateway that is forwarded to a message broker. Further
processing of this data occurs through different software artifacts with analytical capabilities. The
processed data is then stored in a non-relational store adapted for Big Data processing.


## Deployment

Make sure a valid deployment has been created using the deployment tool found in the main scenario folder. The deployment should consist of one global and local with the respective providers and adaptors (this providers and adaptors are fixed by the deployment tool for this scenario).

We assume that Pizza.js (slice-management-client) has been installed on your machine. Set the URL of the deployed global management service with the 
command `$ pizza config -s` and follow the prompts.

Make sure you copy all the files in this directory to a new directory as Pizza will overwrite files upon creating/updating slices.

## Step 1

The inital data pipeline has been created by the System Admin. This initial slice is in the file `slice.json` and consists of:

* MQTT Broker - transport the data from data sources to analytics
* Analytics Client - a simple analytics client that does nothing
* BTS temperature sensors - temperature datasource
* BTS humidity sensors - humidity datasource

Run the command `$ pizza create slice slice.json` to create the slice.

## Step 2 

Run the script `$ ./external_sink.sh`. This script starts a HTTP echo server. Our analytics client in this use case forwards
the data to an external HTTP endpoint.

Wait a few minutes and run `$ kubectl get service external-client -o jsonpath={.status.loadBalancer.ingress[0].ip}` to find the 
external IP of the external sink

## Step 3

In `slice.json` update the HTTP egressAccessPoint of the `analytics` resource to the IP address  and port (3000) of the 
external sink. 

Run `$ pizza slice update slice.json` to update the slice. In a short moment you should see activity in the external data 
sink that you started in step 2

## Step 4

Run the script `$ node shortcut.js`.

This script configures the firewall description in `firewall.json` with the appropriate resource uuids of the affected resources
which are the analytics and client and broker. The firewall is applied to the analytics client, which blocks all incoming
and outgoing traffic by default, so the communication with the broker must be manually maintained.

The script writes these changes to the `slice.json` file, simply run `$ pizza slice update slice.json` to update the slice. In 
a few short moments you should see no more activity in the external sink from step 2 meaning that the firewall has blocked 
access.

## Step 5

You can allow access again to the external sink by adding its IP address (with CIDR e.g. 127.0.0.1/24) and port to the firewall
resource in the `slice.json` file. After an update, activity will begin again in the external sink





