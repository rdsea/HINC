# Interoperability Service

## Overview
This package contain core functions of the Interoperability Service.

The Interoperability Service can be queried to check a slice for interoperability errors and to get interoperability recommendation for a slice.

For now, it is just tested with regards to protocol issues, dataformat issues and structural issues that occur when mqtt connections do not use a messagebroker.

## Configuration

The configuration is within config/ directory. For a production environment, the configuration is set by:

* config/production.json: check the sample file
* export NODE_ENV=production

when NODE_ENV is not set, the configuration will be read from config/default.json.

## Running Local
Install node_modules with : `npm install`

A swagger-api is available at **localhost:8081/api-docs**.
The swagger-api is not complete yet and contains some faults, but it will be updated soon. The core service-routes work though.  

## Run as a docker

If the docker build does not have config/production.json,  
the configuration file, production.json, must be mapped to the right configuration directory: /rsihubintop/config/production.json.

Example of running:

 $docker run -it --rm  -v /var/temp/config:/rsihubintop/config/ -e "NODE_ENV=production"  -p 8081:8081 rsihubintop npm start


## Run tests
`npm test`


## How to use the interoperability service using pizza.js?
  check: run `pizza intop check <file>`

  recommendation: run `pizza intop recommendation <file>`

  where:

  |`<variable>`| description | example |
  |:--- |:--- | :---|
  | `<file>` | the file that contains the slice information | for examples see https://github.com/SINCConcept/HINC/tree/master/interoperability-service/client_testslices |


  for further information: run `pizza intop --help`
