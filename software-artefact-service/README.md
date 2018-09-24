# Software Artefact Service

## Preconditions

The latest versions of the modules `common` and `repository` are installed.
To install the latest versions of `common` and `repository`, either run `mvn clean install` for the respective modules or for the whole project. 

## Build

1. `mvn clean install`
2. `docker build -t software-artefact-service .`

## Run
`docker run -it --rm -p 8082:8082 software-artefact-service`


## Swagger Interface
http://localhost:8082/swagger-ui.html


## pizza.js: how to interact with the service

pizza.js, the slice management client can be used to interact with the software artefact service.

to see the supported commands use `pizza artefact --help`

####How to populate the software artefact service using pizza.js?
run `pizza artefact create <software-artefact> <execution-environment> <metadata-file> <name>`

where:

|`<variable>`| description | example |
|:--- |:--- | :---|
| `<software-artefact>` | either url of a software artefact or a file. if it is a file, then the file will be uploaded to google storage | for examples see `flow_*.json` files of https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/node_red_dataflows |
| `<execution-environment>` | the environment that the artefact runs in. currently supported is **nodered** . check updates if any other are already supported | nodered|
| `<metadata-file>` | a file that contains the interoperability metadata of the software artefact | for examples see `metadata_*.json` files of https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/node_red_dataflows |
| `<name>` | the name of the artefact | nodered_csv_to_json|

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