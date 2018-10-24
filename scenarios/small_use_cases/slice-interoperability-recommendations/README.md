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
    contains contract information about the regulation acts and laws that apply to a component of a slice. In this fictitious example the camera resource requires that the data is only sent to components that fall under the law enforcement of the European Union. As one datastorage is located in the US, the interoperability check prototype will report a problem.

    To run this example use:
    ```
    $pizza intop 03_datacontract_jurisdiction.json
    ```

* **Data Rights**
     is the subcategory that information about how the data is allowed to be used. In the example, the sensor data can only be used non-commercially. If the creator of the Slice wants to use it for a commercial purpose, the Data Contract interoperability is violated.
    
    To run this example use:
    ```
    $pizza intop 04_datacontract_datarights.json
    ```
    
* **Pricing**
    information is needed when resources require payment to access them. This fairly simplified example shows that pricing also plays a role when analysing the interoperability of slices.

    To run this example use:
    ```
    $pizza intop 05_datacontract_pricing.json
    ```

### Quality of Service Interoperability
when Quality of Service measures are not met, the slice or some components of the slice might fail to work. We regard such failures as problems in Quality of Service Interoperability. 

* **Reliability**

    Critical slices or slice components might require reliable datasources in order to work properly. The reliability of a component might determine the usefulness of a component. As is in this example, where Gate Access Sensors of a certain reliability are required to get a reliable stream of information that can then be used to create time- and money-efficient logistics schedules. 
    
    To run this example use:
    ```
    $pizza intop check 06_qos_reliability.json
    ```

* **Message Frequency**

    Some components might only be able to handle a certain amount of messages per second in order to work properly. This example models a weather analytics application that requests sensor data but can not handle the amount of messages it receives.   

    To run this example use:
    ```
    $pizza intop check 07_qos_messagefrequency.json
    ```

### Quality of Data Interoperability

Quality of Data measures can determine if data is actually valuable and can or can not be used for a certain application. If the data does not satisfy required quality measures, the Quality of Data Interoperability is violated. 

* **Precision**
    
    In this fictitious example, an analytics application requires that the data sources, for instance temperature sensors, measure with a certion precision in order to get precise analytics results. The prototype should therefore report problems for all datasources that have a lower measuring precision.

    To run this example use:
    ```
    $pizza intop check 08_qod_precision.json
    ```
    
* **Average Measurement Age**

    The average measurement age might determine if data is still relevant and can therefore be used, once the data has been received. This example models a logistics application that requires tracking data from a seaport. The logistics application requires that the data it uses for scheduling certain tasks is actually relevant within the current time frame.   

    To run this example use:
    ```
    $pizza intop check 09_qod_averagemeasurementage.json
    ```