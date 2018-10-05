# rsiHub Adaptors

These adaptors are responsible for interfacing with different providers to retrieve resource and provider information as well as send controls to the providers to provision/configure resources

## Implementation Details

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

## Adaptor Messaging Protocol

This section documents the messaging protocol (i.e. message types) between an adaptor and a local management service

* `QUERY_RESOURCES` : A resource query, this is sent by the local with no payload. It forces the adaptor to
query the provider for all available resources

* `UPDATE_RESOURCES`: The reply to QUERY_RESOURCES. Is sent by the adaptor. The payload is a JSON list of Resource documents

* `QUERY_PROVIDER`: A provider query, this is send by the local with no payload. It forces the adaptor to
query the provider for all available resources (i.e.) resources that can be provisioned

* `UPDATE_PROVIDER`: The reply to QUERY_PROVIDER. Is sent by the adaptor. The payload is a JSON document describing the provider and available resources

* `REGISTER_ADAPTOR`: The adaptor sends this message to an available local management service to register. The payload is a json document
`{"adaptorName":"<name of the adaptor>"}` the adaptorName should be the the ID of the adaptor as well as the name of the queue it is listening on


The following message types are controls to send to the resource providers

* `PROVISION`: A control to provision a particular resource. The payload is a JSON document describing a resource to be provisioned. This message
is sent from the local to the correct adaptor (the adaptor id is found in the resource document). Expects a Resource JSON as return

* `DELETE`: A control to delete a particular resource. The payload is a JSON document describing a resource to be deleted. This message
is sent from the local to the correct adaptor (the adaptor id is found in the resource document). Expects a Resource JSON as return

* `CONFIGURE`: A control to configure a particular resource. The payload is a JSON document describing a resource to be configure. This message
is sent from the local to the correct adaptor (the adaptor id is found in the resource document). Expects a Resource JSON as return

* `GET_LOGS`: A control to fetch the logs of a particular resource. The payload is a JSON document describing a resource. This message
is sent from the local to the correct adaptor (the adaptor id is found in the resource document). Expects a JSON document with log metadata as return

* `CONTROL_RESULT`: This message is sent from the adaptor to the Local on control execution the result can be successful or a failed one.
The payload of the message depends on what the control expects or an error object if there is an error

## Message Types

To find out the appropriate message Types check the common project in this repo
