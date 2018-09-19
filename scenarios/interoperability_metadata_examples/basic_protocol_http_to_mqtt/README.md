#Basic Interoperability Metadata Example - Protocol
A basic example that only focuses on protocols.
Can be used to merge with a full interoperability metadata example.

The interoperability metadata describes an element that pulls data from an http endpoint and pushes it to an mqtt topic. 

##Metadata Description

####HTTP-Input

| key | assigned value | description|
|:--- |:---------------| :--------: |
| push_pull | "pull" | the input pulls the data itself from a source|
|protocol|an http protocol object| type identified by protocol_name|
|**protocol** |||
|.protocol_name| "http" | the name of the protocol, used to determine the type of the protocol object|
|.uri|"http://localhost:8082/softwareartefacts"| the location of the source, where the data is pulled from |
|.http_method| "GET" | http method that is used to pull the data|


####MQTT-Output
| key | assigned value | description|
|:--- |:---------------| :--------: |
| push_pull | "push "| the output pushes the data to a destination |
|protocol|a mqtt protocol object| type identified by protocol_name|
|**protocol** |||
|.protocol_name| "mqtt"| the name of the protocol, used to determine the type of the protocol object |
|.uri| "mqtt://localhost:1883" | location of the destination, where the data is pushed to |
|.topic|"noderedtest"| mqtt topic of the destination|
|.qos|0| qos that is used when pushing the data | 