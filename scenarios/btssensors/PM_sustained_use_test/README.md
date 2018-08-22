## Deployment

Deployment of the entire testbed must take place in the same google cloud platform project! this is to avoid having manual IP configuration for each component as we can take advantage of the GCP's internal DNS.

This tests requires a deployment of rsiHub that consists of the following:

* 1 Global Management Service
* 1 Local Management Service 

we deploy one instance of the following providers along with one adaptor:

* sensor provider
* mqtt provider
* kubefirewall provider
* bigQuery provider
* ingestion provider
* nodered provider
* cloudmqtt provider

The code for each of the above providers can be found at `https://github.com/rdsea/IoTCloudSamples/tree/master/IoTProviders`.

The code for the provider adaptors can be found at `https://github.com/SINCConcept/HINC/tree/master/ext-plugin`



# Build user clients

The `/user_client` folder contains the source code for the user client. execute the `docker-build.sh` to build this container and push it to the remote repository.

The user clients simulates a user with each container and will send requests to rsiHub. They will make HTTP calls to the test master during their lifecycle to record response times.

# Build test master

The `/master` folder contains the source code for the test master. execute the `docker-build.sh` to build this container and push it to the remote repository.

# Deploy

In the deploy folder there are two files to execute `deploy_master.js` and `deloy_user.js`. These scripts deploy kubernetes deployements of both the master and the users respectively. 

Modify the variables directly in the folder to inject configuration to the containers through environment variables. 

# Obtain Results

The results will be stored in a MongoDB database (configured in the deploy scripts) under the database name `performance`
