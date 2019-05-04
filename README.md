# Harmonizing IoT, Network Functions and Cloud: Resource Slice Interoperability.

This software prototype implements concepts of ensembles of IoT, Network Functions and CLoud Resources. If you use the software, pls. cite the following relevant papers:

- Hong-Linh Truong, Lingfan Gao, Michael Hammerer, Service Architectures and Dynamic Solutions for Interoperability of IoT, Network Functions and Cloud Resources [Preprint](https://www.researchgate.net/publication/326317224_Service_Architectures_and_Dynamic_Solutions_for_Interoperability_of_IoT_Network_Functions_and_Cloud_Resources), 12th European Conference on Software Architecture, September 24-28, 2018, Madrid, Spain

- Hong-Linh Truong, Towards a Resource Slice Interoperability Hub for IoT, [Preprint PDF](http://www.infosys.tuwien.ac.at/staff/truong/publications/2018/rsihub_draft_jan18.pdf), 3rd edition of Globe-IoT 2018: Towards Global Interoperability among IoT Systems, IEEE, 2018. Orlando, Florida, USA, April 17-20, 2018.

- Duc-Hung Le, Nanjangud Narendra, Hong-Linh Truong HINC - Harmonizing Diverse Resource Information Across IoT, Network Functions and Clouds [Submitted PDF](http://bit.ly/1Y36tIY), The IEEE 4th International Conference on Future Internet of Things and Cloud (FiCloud2016), 22-24 Aug, 2016, Vienna,
Austria.

## Modules

### rsiHub Global Management Service

This module can be found in the directory `global-management-service`.

The module is a spring boot application. It serves as a RESTful API interface to query and control resources and their providers through AMQP.

View the README.md in the project for more information about startup and configuration

### rsiHub Local Management Service

This module can be found in the directory `local-management-service`

The module is a spring boot application. It uses AMQP to query and send controls to different
resource providers through the use of adaptors found in `ext-plugin`. The local itself is a microservices
whose opertions can be utilized purely through AMQP in the messaging protocol described below. However, the global management service
is designed to expose these operations through a RESTful API.

View the README.md in the project for more information about startup and configuration

### Repository

This module contains all database specific libraries for the global and local management services

### Common

This is a java library containing all common modules and interfaces for the other java projects

### Adaptor/ext-plugin

This directory houses all the adaptor projects. Adaptors are standalone applications that communicate with a given
local management service through AMQP (messaging protocol described below). The technology stack for an adaptor implementation
is left at the discretion of the user as long asn the messaging protocol is followed. One adaptor interfaces with one Resource Provider

### rsiHub Slice Management Client (A.K.A pizza.js)

This node.js project is a CLI that interfaces with a global management service to create and manage resource slice.
The CLI is documented in the typical POSIX fashion, simply append `--help` at the end of each command to view usage instructions.

Simply go the the project, run `npm install` to install dependencies and `node pizza.js` to run the client.

### rsiHub Software Artefact Service

[This module](software-artefact-service) is used to manage metadata about interoperability software artefacts. Software artefacts are stored in typical repositories like git or docker hub.

### rsiHub Interoperability Service

[The Interoperability Service module](interoperability-service) is used to check interoperability for resources and suggest interoperability bridge.

### Scenarios

This module includes various scenarios for demonstrating resource slice concepts.

### examples

This node.js project is designed to help a developer avoid all the configuration heavy steps to deploy a working system.
On runnning this application, it will ask for several configuration parameters (i.e. AMQP broker connection string) and proceed
to autogenerate configuration for globals, locals, adaptors, providers etc... and make them all available in a single
`docker-compose.yml` file

## Compilation

### Java Modules

* global-management-service
* local-management-service
* common
* respository
* software-artefact-service

Compile all java modules with the command `$ maven clean install`

## Nodejs Modules

They are

* interoperability-service
* slice-management-client

Navigate to the proect and run `$ npm install` to install dependencies, follow the provided README.md to start the module

## Contact
Hong-Linh Truong <linh.truong@aalto.fi>

## Acknowledgement

The work is initially supported by the U-TEST (until 2016) and by H2020 INTER-IoT  through the INTER-HINC project (until 2018). Google Cloud has provided credits for running tests.


------------------------
Copyright 2016-, by Service Engineering Analytics team (rdsea.github.io).
Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0).
