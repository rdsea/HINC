# rsiHub global service

## Overview

This package contain core functions of HINC global service.
The global service will integrate various other local services through AMQP. The messaging
protocol is described in the top level of the repo.

On startup the api swagger documentation will be available on `http://localhost:8080/swagger-ui.html`.

On startup the global will also be listening for registration from different locals. Once a
local management service has been registered then all queries to the global api will query the
appropriate local(s)


## Running Local

java -jar global-management-service-0.1.1.jar

## Configuration
You will need to provide an application.properties file in the same directory. A sample can be found in src/main/resources

```dtd
# amqp connection string: set the string to the AMQP URL
spring.rabbitmq.addresses=
#id
hinc.global.id=globalId
# input exchange name (receive messages)
hinc.global.rabbitmq.input=hinc_global_input
# broadcast exchange name (to locals)
hinc.global.rabbitmq.output.broadcast=hinc_global_broadcast
# groupcast exchange name (to locals)
hinc.global.rabbitmq.output.groupcast=hinc_global_groupcast
# unicast exchange name (to locals)
hinc.global.rabbitmq.output.unicast=hinc_global_unicast


spring.jackson.deserialization.fail-on-unknown-properties=false


```

## Limitation
- Secure HTTPS
