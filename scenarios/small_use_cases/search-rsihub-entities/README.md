# Under construction
# TODO
* setup
* verify commands

# Search rsiHub Resources, Software Artefacts and Interoperability Bridges

We show a couple of examples on how to search rsiHub Resources, Software Artefacts and Interoperability Bridges using rsiHub's CLI **pizza.js**

### For deployment

### For this tutorial, we

* use rsiHub's CLI pizza.js to search resources and software artefacts
* search software artefacts with rsiHub's interoperability metadata


## Developers/User Story

Assume you want to search Resources or Software Artefacts, for instance to solve an interoperability issue. By using rsiHub's CLI **pizza.js**, developers can search rsiHub entities with document queries that use mongodb query syntax. In particular, when searching for rsiHub components for interoperability purposes, rsiHub's interoperability metadata can help finding suitable components. 

### Search resources

* **search all mqttbroker resources**

  
```
$pizza resource query '{"metadata.resource.type.prototype":"messagebroker", "metadata.resource.type.protocols.protocol_name":"mqtt"}'
```


### Search interoperability software artefacts

* **search all software artefacts that are executed with docker**
```
$pizza artefact search '{"executionEnvironment":"docker"}'
```

* **search all software artefacts of which the name starts with 'nodered_json_to'**
```
$pizza artefact search '{"name":{"$regex":"nodered_json_to_.*"}}'
```

* **search all software artefacts that are executed with nodered, have an json-Input and an csv-Output**
```
$pizza artefact search '{"executionEnvironment":"nodered", "metadata.inputs.dataformat.dataformat_name":"json", "metadata.outputs.dataformat.dataformat_name":"csv", "metadata.outputs.dataformat.headers_included":true, "name":{"$regex":"nodered_json_to_.*"}}'
```

### Show the interoperability software artefact, using the artefact reference

Suppose that the developer wants to view the previously searched nodered flow directly in the command line. This can be done by adding ```-r``` or ```--reference``` to the command, and additionally wrapping it in a ```curl call```. The complete example will then look like this:

```
curl -X GET $(pizza artefact search '{"name":"nodered_json_to_csv_flow"}' -r)
```
