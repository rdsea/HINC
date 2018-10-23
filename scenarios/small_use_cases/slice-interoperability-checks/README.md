# Under construction

# Check SliceInformation for Interoperability Issues

We show a couple of interoperability-check examples on fictitious SliceInformation that is possible with interoperability metadata.

### For deployment

### For this tutorial, we

* check the interoperability of fictive slices based on the interoperability metadata

### Preconditions

* rsiHub CLI (pizza) installed. check with: ```$pizza --version```
* rsiHub Interoperability Service running 

## Developers/User Story

Assume that a developer wants to check if the SliceInformation is interoperable before actually provisioning the Slice. This can be done manually, but in order to decrease the effort of checking the SliceInformation, rsiHub's interoperability check assists the developer in checking for interoperability.

Interoperability can fail on multiple levels, some of which are too complex to clear automatically and require human intelligence. Our focus lies on assisting users with detecting problems on the level of:
* protocol interoperability
* dataformat interoperability
* data contract interoperability
* quality of service interoperability
* quality of data interoperability

For this use case we provided a couple of exemplary SliceInformations that contain problems on the respective interoperability levels. While they all showcase one tiny particular interoperability problem, a multitude of interoperability problems exist for each level.

### Protocol Interoperability
Protocol interoperability describes the failure to communicate on the protocol layer, which is right above the physical layer. Protocol interoperability can be violated if two components implement different communication protocols, but misconfiguration of protocols can also be an issue.

To run this example use:
```
$pizza intop check 01_protocol.json
```

### Dataformat Interoperability
Dataformat interoperability is violated if two components of a slice fail to use the same dataformat. Thus, the binary data is delivered correctly but it is not interpreted correctly. Dataformat interoperability might also be violated between components that are not directly connected with each other (for instance consider two components that communicate via messagebroker).

To run this example use:
```
$pizza intop check 02_dataformat.json
```

### DataContract Interoperability
If the datacontract between slice components or even the slice in general is violated, then we also view the datacontract interoperability to be violated.

For rsiHub we currently consider three different categories of DataContract interoperability:

* **Jurisdiction**

    To run this example use:
    ```
    $pizza intop 03_datacontract_jurisdiction.json
    ```

* **Data Rights**

    To run this example use:
    ```
    $pizza intop 04_datacontract_datarights.json
    ```
    
* **Pricing**

    To run this example use:
    ```
    $pizza intop 05_datacontract_pricing.json
    ```

### Quality of Service Interoperability

* **Reliability**
    To run this example use:
    ```
    $pizza intop check 06_qos_reliability.json
    ```

* **Message Frequency**

    To run this example use:
    ```
    $pizza intop check 07_qos_messagefrequency.json
    ```

### Quality of Data Interoperability

* **Precision**

    To run this example use:
    ```
    $pizza intop check 08_qod_precision.json
    ```
    
* **Average Measurement Age**

    To run this example use:
    ```
    $pizza intop check 09_qod_averagemeasurementage.json
    ```