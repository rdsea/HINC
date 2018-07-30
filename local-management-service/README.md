# rsiHub Local Management Service

## Overview
This application is reponsible for marshalling resource and provider data as well
as forwarding controls to the various resource providers (e.g. provision, reconfigure...).

It uses and AMQP as communication method between the different adaptors deployed. The messaging
protocol is documented at the top level of this repository.

On startup the local management service listens for registration from adaptors, once an
adaptor has been registered, it will be queried for resource data periodically at an interval
defined in the configuration.

For examples on adaptors please visit the directory `/ext-plugin`

## Running Local
java -jar local-management-service-1.0.jar

## Configuration
You will need to provide an application.properties file in the same directory. A sample can be found in src/main/resources

```
# amqp broker connection string
spring.rabbitmq.addresses=
# exchange name of the global
hinc.local.amqp.output=hinc_global_input
# group name
hinc.local.group=test
# local id
hinc.local.id=local
# determines how often the local queries resources from its adaptors
hinc.local.refresh=10000
# name of the input exchange for the local (receives messages)
adaptor.amqp.input=adaptor_local_input
# broadcast exchange for adaptors
adaptor.amqp.output.broadcast=adaptor_local_broadcast
# unicast exchange for adaptors
adaptor.amqp.output.unicast=adaptor_local_unicast

# these settings bear no significance to functionality but are required
spring.jackson.deserialization.fail-on-unknown-properties=false
server.port=12342
```

## Limitation
- Secure HTTPS
