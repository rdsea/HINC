# Basic Interoperability Metadata Example - Prototype MessageBroker

A basic example that only focuses on one messagebroker prototype.
Can be used to merge with a full interoperability metadata example.

The interoperability metadata describes an mqtt messagebroker. 

## Metadata Description

| key | assigned value | description|
|:--- |:---------------| :--------: |
|resource.category|"network_function"||
|resource.type|a messagebroker type object| type identified by prototype|
|**type**|||
|.prototype|"messagebroker"|the name of the type object|
|.protocols|[\<protocol object\>]| a list of protocols that the messagebroker supports. in our case it's an mqtt protocol object|
|.protocols[0].uri|"mqtt://192.168.99.101:1883"||
|.protocols[0].protocol_name|"mqtt"||
|.topics|["noderedtest"]| a list of topicnames that are already available|
|.auto_create|true| whether a new topic is automatically created or not|