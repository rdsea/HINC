## HINC - Harmonizing Diverse Resource Information Across IoT, Network Functions and Clouds.

**Target users:**  Software agents (edge/cloud app), global management services (resource provisioner) and developers/administrators. 

**Features:**

1. Capture information of IoT and network resources is abstracted by means of two main entities:
  * Software-defined gateway: give information of data, control and connectivity, reflect IoT devices connecting to the gateway. E.g. the information about data from many sensors transfering through the gateway.
  * Virtual router: give information about the network link. In support virtual router, the information about network graph, and end-to-end network service can be generated.
  
2. Query management: users can query only needed information. E.g.: to query datapoints about temperature data/control in such a location/building. User can use one-time query or subscription to get changes. This function is partly implemented.

3. Information-centric communication and query: the framework suppports routing a query to the right information providers to get the data. The underlying protocol is AMQP, using RabbitMQ.

4. Integrate more adaptors. To implement new adaptors, two mechanism is need: (1)an information collector and validation, and (2)an information transformer to map the information. The implemented adaptor for experiments includes: Android sensors(API/file-based), OpenIOT service (API/file-based), weave-virtual-router (commandline output).

5. Capture the relationships. User can query at the global resource management, receive the list of relationships between components.

**Usecase**

The flow of usage the framework as following:
1. User deploy the Local Management Services on gateways/cloud, then configure the information providers and message queue.
2. User run the Global Management Service on the cloud or personal laptop, configure it to connect to the message queue.
4. User can execute a query from the client, which interact with Global Management Service.

** Reference **
If you are using HINC for your work, pls. cite the following paper:

D.-H. Le, N. Narendra, and H.-L. Truong, “Hinc harmonizing diverse resource information across iot, network functions and clouds,” in Proceedings of the 2016 International Conference on Future Internet of Things and Cloud (FICLOUD ’16), 2016, submitted.
------------------------
Copyright 2016, by TU Wien.
Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0).



