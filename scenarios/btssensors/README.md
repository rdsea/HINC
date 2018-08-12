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





