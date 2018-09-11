# Interoperability Service

## Overview
This package contain core functions of the Interoperability Service.

The Interoperability Service can be queried to check a slice for interoperability errors and to get interoperability recommendation for a slice.

For now, it is just tested with regards to protocol issues, dataformat issues and structural issues that occur when mqtt connections do not use a messagebroker.

## Running Local
Install node_modules with : `npm install`

Run instance with: `npm start`. Currently it is hardcoded to run at **localhost:8081**. 
A swagger-api is available at **localhost:8081/api-docs**.
The swagger-api is not complete yet and contains some faults, but it will be updated soon. The core service-routes work though.  

## Configuration
At the current state, when searching resources that can be used to solve an interoperability problem, the recommendation algorithm queries a mongodb collection. 
For now, this mongodb collection is currently hardcoded in `interoperability-service/src/main/recommendation/intop_recommendation.js`
Change the config parameters if you want to use a different mongodb collection.

## Run tests
`npm test`