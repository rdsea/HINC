# Artefact Interoperability Metadata - NodeRed csv to json

## Artefact Description

A NodeRed Flow that transforms csv data to json. It uses MQTT as communication protocol, and the connection properties are already configured within the artefact

see: https://github.com/rdsea/IoTCloudSamples/tree/master/IoTCloudUnits/node_red_dataflows/csv_to_json_flow

## Metadata Description

#### Input:
 * Protocol: MQTT (configured in the artefact)
 * Dataformat: csv, headers not included but defined in the artefact
 
#### Output:
 * Protocol: MQTT (configured in the artefact)
 * Dataformat: json