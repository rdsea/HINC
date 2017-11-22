---
layout: page
title: "Develop plugins"
category: dev
date: 2016-06-23 13:20:12
---

If you need to connect resources which are managed by new type of provider or device, HINC can adapt easily. Followings are steps to write plug-in and run HINC local.

** Step 1: Clone HINC, build and install to local Maven **

* Requirement:* You will need *git*, *maven* and *JDK 1.7+*

Following commands will do the job:

```sh
git clone https://github.com/SINCConcept/HINC.git
cd HINC
mvn clean install
```

** Step 2: Create a Java project and import dependencies **

Create a Maven Java project with your preferred IDE (e.g. [Netbeans](https://platform.netbeans.org/tutorials/nbm-maven-quickstart.html) or [Eclipse](http://www.mkyong.com/maven/how-to-create-a-java-project-with-maven/)). 

Import following into your pom.xml, be careful if you already have `<dependencies>` tag:

```
    <dependencies>
        <dependency>
            <groupId>at.ac.tuwien.dsg.hinc</groupId>
            <artifactId>collector</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
```

** Step 3: Implement the adaptor **

An adaptor for new type of provider/resource needs to implement 2 types of plug-in via following interfaces:

 - *Plug-in for provider/device API*: enables HINC to query resources by calling the provider/device API via [pull-based interface](https://github.com/SINCConcept/HINC/blob/master/collector/src/main/java/sinc/hinc/abstraction/ResourceDriver/ProviderQueryAdaptor.java) or let the provider/device updates information to HINC via [push-based interface](https://github.com/SINCConcept/HINC/blob/master/collector/src/main/java/sinc/hinc/abstraction/ResourceDriver/ProviderListenerAdaptor.java). API can be RESTful or dirrect command line, which depends on who is managing the resources.
 

 - *Plug-in for transforming information*: Via [informaion transformer interface](https://github.com/SINCConcept/HINC/tree/master/collector/src/main/java/sinc/hinc/abstraction/transformer), HINC can abstract the information to its unified model.

 Please check some examples of adaptors for: [virtual resource](https://github.com/SINCConcept/HINC/tree/master/ext-plugin/TEITPlugin), [Dummy provider](https://github.com/SINCConcept/HINC/tree/master/ext-plugin/DummyProvider), [OpenHAB provider](https://github.com/SINCConcept/HINC/tree/master/ext-plugin/TransformOpenHAB) or [Weave virtual router](https://github.com/SINCConcept/HINC/tree/master/ext-plugin/TransformWeaveRouter)

** Step 4: Build the new plugin and add to HINC Local **

You need to build your adaptor and get the .jar achieve. Along with the whole HINC project you built at *step 1*, now it is ready to run HINC Local. [Please check how to run it here](http://sincconcept.github.io/HINC/doc/installation.html). To test your adaptor, please try to setup a testbed, or write your applicaiton on the resource.

Note: HINC will automatic scan your new plug-in in Java classpath. All you need to do is edit the configuration in `source.conf` file. Please check the instalation document for the details.
