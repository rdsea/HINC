---
layout: page
title: "Programming"
category: doc
date: 2016-09-22 11:57:58
order: 4
---


HINC works as a distributed information grid, that harmonizing resources from different places. Application on top of HINC can access resources in very a flexible manner, which provide a first step to the virtualization of end-to-end slicing.

Followings are some example of programming in Java with HINC model and HINC API. The code of the example can be found in [examples folder](https://github.com/SINCConcept/HINC/tree/master/examples/DemoApplication)

### Preparation

 - You need a testbed to test the code. Please refer to the testbed tutorial to setup one. Current example is using only IoT testbed.
 - Git, Maven, JDK 1.7+, an IDE.
 - Clone HINC, build and install to local Maven. Following commands will do the job:

```sh
git clone https://github.com/SINCConcept/HINC.git
cd HINC
mvn clean install
```

### Create a Java project and import dependencies

Create a Maven Java project with your preferred IDE (e.g. [Netbeans](https://platform.netbeans.org/tutorials/nbm-maven-quickstart.html) or [Eclipse](http://www.mkyong.com/maven/how-to-create-a-java-project-with-maven/)). 

Import following into your pom.xml, be careful if you already have `<dependencies>` tag:

```
    <dependencies>
        <dependency>
            <groupId>at.ac.tuwien.dsg.hinc</groupId>
            <artifactId>communication</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
```

### Code examples

When you have testbed up, HINC entities will run distributedly and communicate via an AMQP broker. The application can use HINC API to send request to the communication world. Some basic steps are shown below:

**Basic query resources:**

First, you need to create a request message. There is 2 important information: the *request type* and the *group*. Based on this, HINC know how to process.

```java
HincMessage request = new HincMessage()
    .hasType(HINCMessageType.QUERY_INFORMATION_GLOBAL)
    .hasSenderID("my-application")
    .hasTopic(HincMessageTopic.getBroadCastTopic("my-group"));
```

Second, a message sender is created that connect to the communication

```java
HINCMessageSender sender = new HINCMessageSender("amqp://localhost", "amqp");
```

Third, use the sender to send the request, and process the result

```java
sender.asynCall(2000, request, new HINCMessageHander() {
    @Override
    public HincMessage handleMessage(HincMessage responseMsg) {
        // extract the IoT Unit in the response message
        WrapperIoTUnit wrapper = new WrapperIoTUnit(responseMsg.getPayload());
        List<IoTUnit> lstUnits = wrapper.getUnits();

        // do something on the new information
        System.out.println("Query " + lstUnits.size() + " units");
        
		//further processes

        return null;
            }
        });
```

**Send a control:**

Let us say application receives the IoT Unit it requires, e.g. some smart lights, now it want to control it.

```java
for (IoTUnit unit : listOfUnits) {
    ControlPoint cp = unit.findControlpointByName("turn-on");
    if (cp != null) {
        HincMessage request2 = new HincMessage()
                .hasType(HINCMessageType.CONTROL)
                .hasPayload(cp.getUuid());
        sender.synCall(request2);
    }
}
```

**Listening to the update:**

Application can also monitor and process the changes of the resources by adding listeners.

```java
HINCMessageListener listener = new HINCMessageListener("amqp://localhost", "amqp");
listener.addListener("my-group", HINCMessageType.UPDATE_INFORMATION.toString(), new HINCMessageHander() {
    @Override
    public HincMessage handleMessage(HincMessage message) {
        IoTUnit unit = IoTUnit.fromJson(message.getPayload());
        System.out.println("Got an update about IoT Unit with ID: " + unit.getResourceID());
        return null;
    }
});
listener.addListener("my-group", HINCMessageType.SYN_REPLY.toString(), new HINCMessageHander() {
    @Override
    public HincMessage handleMessage(HincMessage message) {
        HincLocalMeta meta = HincLocalMeta.fromJson(message.getPayload());
        System.out.println("A new HINC-enabled gateway is discovered: " + meta.getUuid());
        return null;
    }
});

listener.listen();
```