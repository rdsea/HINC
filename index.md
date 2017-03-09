---
layout: default
title: "HINC - Harmonizing IoT, Network functions and Clouds"
---


## Brief

---

HINC implements the lowest layer of [the SINC Concept](http://sincconcept.github.io) by interfacing to different providers to obtain resource metadata and to control resources.
HINC concepts and techniques are described in the paper: Duc-Hung Le, Nanjangud Narendra, Hong-Linh Truong [HINC - Harmonizing Diverse Resource Information Across IoT, Network Functions and Clouds](http://bit.ly/1Y36tIY) ([Submitted PDF](http://bit.ly/1Y36tIY)), [The IEEE 4th International Conference on Future Internet of Things and Cloud (FiCloud2016)](http://www.ficloud.org/2016/), 22-24 Aug, 2016, Vienna, Austria


----

## Scope

HINC framework provides an abstraction layer on top of IoT cloud infrastructure for the IoT cloud application. To this end, HINC focuses on distributed architecture and communication protocol to enable the interoperation of IoT services on both **edge-to-edge** and **edge-to-cloud** networks. HINC brings network functions into the picture that aims to enable the reconfiguration of the network and communication capability among IoT services and cloud services.

![scope](images/scope.png "The deployment scope of HINC framework"){:width="750px"}




----

## Highlight features
----
  
1\. High level view of distributed resources for IoT, Network functions and Clouds.

> HINC proposes a high level model to capture the information of IoT without caring about the low level resources like sensor, actuator and gateway. The novel idea is to focus on high level concept of Software-Defined Gateway, which includes 4 aspects: data, control, connection and execution environment. HINC also targets to novel Network functions models to leverage network slicing technology. Conbining with cloud services and resources, HINC aim to supporting end-to-end slicing techologies crossing IoT, Network functions and Clouds.

2\. Easy-to-use API and management mechanisms for diverse type of available resources.

> HINC interfaces with multiple providers who directly manage the resources (IoT, Network functions and Cloud providers). Via an adaption and tranformation process, the information is abtracted to convert to higher level model. The API enables to route the query to distributed provider to gather the information and to send the control for resources. We are continueing to work on Model-Driven

3\. Information-centric communication and query for large-scale cloud-based IoT systems.

> HINC leverages the message queue for the communication. We are continueing to develop a novel protocol atop of the message-oriented middleware to support information-centric query, aiming to enhance the protocol for complex resource models.

4\. Extensible design for integrating more providers.

> HINC provides a set of interface for querying information (one-time query and subscription), and control IoT resource. Based on such interface, we can add more adaptors to interface more providers. A new provider can join the existing system without any interuption.

HINC is under active development. The followings sections provide overview of current states and ongoing work.


---


## Authors and Contributors
---

- [Duc-Hung Le](http://dsg.tuwien.ac.at/staff/dle/), TU Wien
- [Hong-Linh Truong](http://dsg.tuwien.ac.at/staff/truong/), TU Wien
- [Nanjangud Narendra](https://sites.google.com/site/ncnaren/), Ericsson Research India


Contact: [Hong-Linh Truong](http://dsg.tuwien.ac.at/staff/truong/) at truong@dsg.tuwien.ac.at for further information.

---

Copyright 2016, by TU Wien. Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
