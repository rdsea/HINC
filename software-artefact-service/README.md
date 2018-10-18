# Software Artefact Service

## Preconditions

The latest versions of the modules `common` and `repository` are installed.
To install the latest versions of `common` and `repository`, either run `mvn clean install` for the respective modules or for the whole project. 

## Build

Make sure that you have the right application.properties in the root directory 
or in config subdirectory. 

1. `mvn clean install`
2. `docker build -t rdsea/rsihubsas .`

## Run with docker 

you can pull rdsea/rsihubsas from docker hub or build your own.

Make sure that the application.properties is in the config directory and mapped 
to the docker filesystem. For example, the application.properities can be stored 
in /var/rsihubsas/config in the host machine. Then this directory must be mapped to 
/rsihubsas/config in the docker: 

`docker run -d -v /var/rsihubsas/config:/rsihubsas/config -p 8082:8082 rdsea/rsihubsas`


## Swagger Interface
http://localhost:8082/swagger-ui.html


## pizza.js: how to interact with the service

pizza.js, the slice management client can be used to interact with the software artefact service.

to see the supported commands use `pizza artefact --help`

### How to install pizza.js
1. go to `/slice-management-client` where `pizza.js` is located
2. `npm install -g` 

### How to configure pizza.js
get the current config: `pizza config -g`

update the config: `pizza config -s` 

### How to populate the software artefact service using pizza.js?
run `pizza artefact create <software-artefact> <execution-environment> <metadata-file> <name>`

where:

|`<variable>`| description | example |
|:--- |:--- | :---|
| `<software-artefact>` | either url of a software artefact or a file. if it is a file, then the file will be uploaded to google storage | for examples see `flow_*.json` files of https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/node_red_dataflows |
| `<execution-environment>` | the environment that the artefact runs in. currently supported is **nodered** . check updates if any other are already supported | nodered|
| `<metadata-file>` | a file that contains the interoperability metadata of the software artefact | for examples see `metadata_*.json` files of https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/node_red_dataflows |
| `<name>` | the name of the artefact | nodered_csv_to_json|

### Examples: pizza create artefact

#### Create a nodered flow from a local flow file
Note: **\<IoTCloudSamples>** is the path to the IoTCloudSamples directory

`<path>` = \<IoTCloudSamples>/IoTCloudUnits/node_red_dataflows/json_to_yaml_flow

`pizza artefact create <path>/flow_json_to_yaml.json nodered <path>/metadata_json_to_yaml.json nodered_json_to_yaml`


#### Create a nodered flow from flow file url
Note: **\<IoTCloudSamples>** is the path to the IoTCloudSamples directory

`<path>` = \<IoTCloudSamples>/IoTCloudUnits/node_red_dataflows/csv_to_xml_flow

`pizza artefact create https://raw.githubusercontent.com/rdsea/IoTCloudSamples/master/IoTCloudUnits/node_red_dataflows/csv_to_xml_flow/flow_csv_to_xml.json nodered <path>/metadata_json_to_yaml.json nodered_csv_to_xml`


#### Create a docker artefact from a docker image
Note: **\<IoTCloudSamples>** is the path to the IoTCloudSamples directory

`<path>` = \<IoTCloudSamples>/IoTCloudUnits/datastorageArtefact

`pizza artefact create rdsea/restpull2gspush-bridge docker <path>/metadata.json docker_dataStorageArtefact`


## REST API

Base URL: `http://localhost:8082

| HTTP Method | URL           | Queryparameter | Requestbody (JSON)| Description|
|:------------- |:-------------| :-----: | :-----:| -----:|
| GET | `/softwareartefacts` | `limit` to limit the results | | returns all SoftwareArtefacts. optionally limit the number of results with the queryparameter `limit` |
| PUT | `/softwareartefacts` | | `softwareArtefact` a valid SoftwareArtefact Object | creates a SoftwareArtefact that is specified in the requestbody |
| GET | `/softwareartefacts/search` | `query` a valid mongodb query-by-example | | queries SoftwareArtefacts with a mongodb query-by-example that is specified with the queryparameter `query`|
| POST | `/softwareartefacts/search` | | `query` a valid mongodb query-by-example  |  queries SoftwareArtefacts with a mongodb query-by-example that is specified in the requestbody. alternative implementation of `GET /softwareartefacts/search` in case the query exceeds the allowed length of a queryparameter/URL|
| DELETE | `/softwareartefacts/{id}` | | |  deletes the SoftwareArtefact with id {id}|
| GET | `/softwareartefacts/{id}` | | |  returns the SoftwareArtefact with id {id} |
| PUT | `/softwareartefacts/{id}/metadata` | | `metadata` a valid Metadata Object |  updates the Metadata of the SoftwareArtefact with id {id} |


### cURL Examples
**get three software artefacts** (GET /softwareartefacts?limit=3)

```curl -X GET --header 'Accept: application/json' 'http://localhost:8082/softwareartefacts?limit=3'```


**create a software artefact** (PUT /softwareartefacts)

```
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "artefactReference": "https://storage.googleapis.com/a_storage_bucket_example/flows.json", \ 
   "executionEnvironment": "nodered", \ 
   "metadata": { \ 
         "resource": { \ 
             "category": "iot", \ 
             "type": { \ 
                 "prototype": "software_artefact" \ 
             } \ 
         }, \ 
         "inputs": [ \ 
             { \ 
                 "push_pull": "push", \ 
                 "protocol": { \ 
                     "uri": "mqtt://test:1883", \ 
                     "protocol_name": "mqtt", \ 
                     "topic": "basic_test", \ 
                     "qos": 0 \ 
                 }, \ 
                 "dataformat": { \ 
                     "encoding": "utf-8", \ 
                     "dataformat_name": "csv" \ 
                 } \ 
             } \ 
         ], \ 
         "outputs": [ \ 
             { \ 
                 "push_pull": "push", \ 
                 "protocol": { \ 
                     "uri": "mqtt://test:1883", \ 
                     "protocol_name": "mqtt", \ 
                     "topic": "basic_test", \ 
                     "qos": 0 \ 
                 }, \ 
                 "dataformat": { \ 
                     "encoding": "utf-8", \ 
                     "dataformat_name": "json" \ 
                 } \ 
             } \ 
         ] \ 
     }, \ 
   "name": "nodered csv to json" \ 
 }' 'http://localhost:8082/softwareartefacts'
```


**search softwareartefacts that have an mqtt input set in the metadata** 

```curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"metadata.inputs.protocol.protocol_name": "mqtt"}' 'http://localhost:8082/softwareartefacts/search'```
