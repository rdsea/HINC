---
layout: page
title: "Installation"
category: doc
date: 2016-06-23 13:18:47
order: 3
---

### Build from source

HINC can be build with Java 1.7+ and Maven as followings:

1. Clone the project from [GitHub](https://github.com/SINCConcept/HINC)
2. Run `$ mvn clean install`
3. Take 2 artifacts corresponding to the components: `local-management-service-1.0.jar` and `global-management-service-1.0.jar`.

### Use ready-to-run artifacts

The artifacts can be downloaded from the [artifact branch on Github](https://github.com/SINCConcept/HINC/tree/artifacts).

We try to keep this branch up-to-date with the most stable version. Therefore, use this branch only when you have problems to compile the source code. 

### Run Local Management Service

The Local Management Service is a lightweight daemon running on a device or gateway to collect information. It requires a set of adaptors, which interface with providers or devices API. A set of adaptors can be found on [ext module on GitHub](https://github.com/SINCConcept/HINC/tree/master/ext-pom">Github). Each adaptor is built as a `.jar` archive and put in a same folder with the `local-management-service-1.0.jar` to load under Java classpath. There are 2 configuration files need to be edited. For the concrete contents, please see the next section about testbed setup.

- **hinc.conf**: Contains parameters for HINC Local, e.g. group name, queue service.
- **sources.conf**: Contains paramneters for plugins, e.g. username/password, endpoint.

The local management service can be run with the below command. Note that we need to provide the jar of the local management service and the list of adaptor together.

```sh
$ java -cp "local-management-service-1.0.jar:TranformIoTivity-1.0.jar:TransformOpenHAB-1.0.jar" sinc.hinc.local.Main
```

Please refer to the Tutorial sections to see the details how to setup the whole testbed.
