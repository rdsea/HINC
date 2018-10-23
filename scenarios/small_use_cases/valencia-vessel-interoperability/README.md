# Under construction

#TODO: 
* prepare the files for the use case
* add the actual commands
* run the use case

# Develop Interoperability Solutions - Dataformat Interoperability in an IoT Seaport

We show one example of dataformat and protocol interoperability in an IoT Seaport Scenario

## IoT Seaport

For this example, we assume that we have an Port Control Service(PCS) that manages the Seaport and vessels that approach the Seaport and therefore need to communicate with the PCS.
Suppose that the PCS is initially only capable of using MQTT and interpreting data as if it is in JSON. Additionally, we have two vessels. Vessel A is only capable of using the AMQP communication protocol and JSON. Vessel B uses CSV as dataformat an MQTT as communication protocol.

### For deployment

We deploy a docker stack or use docker-compose on a local machine. After that, we populate the databases with the necessary data.

### For this tutorial, we:

- check a SliceInformation for interoperability problems
- use rsiHub to solve the interoperability problems automatically
- provision a Slice with the created SliceInformation
- update a Slice

### Preconditions
* rsiHub CLI (pizza) installed. check with: ```$pizza --version```
* docker installed. check with: ```$docker --version```

## Developers/User Story

#### *0. Deploy the stack and populate the databases*

1. deploy the stack to a docker swarm or run it with docker-compose:

    ```
    $docker stack deploy -c stack_valencia_use_case.yml
    ```
    
2. populate the databases

    ```
    $./populate_database.sh -url <swarm-ip>
    ```
   
*alternative commands for running it on a local machine:*

```
$docker-compose up -c local_valencia_use_case.yml
    
$./populate_database.sh -url localhost
```

#### 1. Create an initial SliceInformation with PCS, a messagebroker and Vessel A

We assume that PCS, messagebroker and vessels can either be provided or are already running (PCS), so that we can use them for our slice. 

View the available SliceInformation:

```
$cat sliceinfo_valencia_A.json
```

#### 2. Check if the SliceInformation is interoperable

To find out if the components of the SliceInformation can communicate, or if there are detectable problems at hand, we check the interoperability of the SliceInformation. 

```
$pizza intop check sliceinfo_valencia_A.json
```

#### 3. Check if rsiHub can provide an automatic solution recommendation

By using rsiHub's interoperability check, we detected that Vessel A can not communicate with MQTT as it only supports AMQP. We need to correct the SliceInformation in order to mediate between MQTT and AMQP. We could do that manually but first, let's see if rsiHub's interoperability recommendation can find a solution to our problem.

```
$pizza intop recommendation sliceinfo_valencia_A.json
```

#### 4. Provision the slice

Provision the Slice by using rsiHub.

```
$pizza slice create sliceinfo_valencia_A.json
```

#### 5. Vessel B approaching: add Vessel B to SliceInformation

Suppose that vessel B is approaching and also needs to communicate with the PCS. We therefore want to add it to our slice so that we can provision the communication. 

In the file <todo>, vessel B is already added to the slice.

```
$cat sliceinfo_valencia_B.json
```

#### 6. Check if the updated SliceInformation is interoperable
Before provisioning the communication, we want to check if the PCS and the vessel can establish a working communication channel. Same as above, we use rsiHub's interoperability check to see if there are any detectable issues.

```
$pizza intop check sliceinfo_valencia_B.json
``` 

#### 7. Check if there is an automatic recommendation

Despite using an interoperable communication protocol, the interoperability check detected that the PCS and Vessel B can not fully communicate because the PCS uses JSON and Vessel B uses CSV as communication protocol.

We have to fix this interoperability issue by mediating between the PCS and Vessel B. By using rsiHub's interoperability recommendation, we test if there is a recommended automatic solution available.

```
$pizza intop recommendation sliceinfo_valencia_B.json
```

#### 8. Update the provisioned Slice with the new SliceInformation

We update our previously provisioned slice with the new interoperable SliceInformation that the interoperability recommendation altered.

```
$pizza slice update sliceinfo_valencia_B.json
```