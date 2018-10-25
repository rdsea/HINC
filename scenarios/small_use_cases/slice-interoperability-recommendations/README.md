#Under construction

#TODO
* setup
* verify commands and run examples

# Get Interoperability Recommendations for a SliceInformation

We show a couple of interoperability-recommendation examples on fictitious SliceInformation that is possible with interoperability metadata.

### For deployment

### For this tutorial, we

* get automatic interoperability solutions for fictive slices based on the interoperability metadata

### Preconditions

* rsiHub CLI (pizza) installed. check with: ```$pizza --version```
* rsiHub Interoperability Service running 

## Developers/User Story

Assume that a developer wants to solve the interoperability problems that a previous interoperability check has found. This could be done manually, but in order to decrease the effort of solving interoperability problems of SliceInformations, rsiHub's interoperability recommendation is capable of solving certain interoperability problems automatically. Before solving the issue manually, the developer therefore will try to solve the problem automatically, as this requires less effort if a solution can be found.

For this use case we provided a couple of exemplary SliceInformations containing interoperability problems that can be solved automatically if sufficient Resources and Interoperability Software Artefacts are available.

### Protocol Interoperability
Protocol interoperability describes the failure to communicate on the protocol layer, which is right above the physical layer. Protocol interoperability can be violated if two components implement different communication protocols, but misconfiguration of protocols can also be an issue.

In this example, protocol mediators will be added to the SliceInformation.

To run this example use:
```
$pizza intop recommendation 01_protocol.json
```

### Dataformat Interoperability
Dataformat interoperability is violated if two components of a slice fail to use the same dataformat. Thus, the binary data is delivered correctly but it is not interpreted correctly. Dataformat interoperability might also be violated between components that are not directly connected with each other (for instance consider two components that communicate via messagebroker).

In this example, Interoperabilty Software Artefacts that are capable of transforming the dataformat will be added to the SliceInformation.

To run this example use:
```
$pizza intop recommendation 02_dataformat.json
```

### Quality of Service Interoperability - Message Frequency

Some components might only be able to handle a certain amount of messages per second in order to work properly. This example models a weather analytics application that requests sensor data but can not handle the amount of messages it receives.   

In this example, an Interoperabilty Software Artefacts that buffers the last received message and forwards it at a fixed, slower rate will be added to the SliceInformation.

To run this example use:
```
$pizza intop recommendation 07_qos_messagefrequency.json
```
