# rsiHub Adaptors

These adaptors are responsible for interfacing with different providers to retrieve resource and provider information as well as send controls to the providers to provision/configure resources

# Implementation Details

The technology stack of each adaptor is up to the developer. However the adaptor must communicate with a Local Management Service through AMQP (RabbitMQ).

The Local Management Service communicates with its adaptors through a Fanout exchange. Messages will have the appropriate reply_to header set, so
there is no reason to configure any kind of routing keys.

The adaptor must register to the Local Management Service with the following message:

```
{
    msgType: 'REGISTER_ADAPTOR',
    senderID: adaptorName,
    receiverID: null,
    payload: payload,
    timeStamp: current timestamp,
    uuid: '',
}
```

The queue that the adaptor consumes from must be named after the adaptor name (for routing purposes). The queue is correctly bound by the Local
Management Service upon registration


# Message Types

To find out the appropriate message Types check the common project in this repo
