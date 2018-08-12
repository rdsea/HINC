# Use Case - New Data Consumer

This use case represents the stakeholders of the System Admin and Data Consumer (client who wishes to use BTS data). In this use case, a new client 
approahces the System Admin for permission to consume data. The System Admin creates a data pipeline for the Data Consumer. The Data Consumer then 
searches for the required data sources to add to his data pipeline.

## Deployment

Make sure a valid deployment has been created using the deployment tool found in the main scenario folder. The deployment should consist of one global and local with the respective providers and adaptors (this providers and adaptors are fixed by the deployment tool for this scenario).

We assume that Pizza.js (slice-management-client) has been installed on your machine. Set the URL of the deployed global management service with the 
command `$ pizza config -s` and follow the prompts.

Make sure you copy all the files in this directory to a new directory as Pizza will overwrite files upon creating/updating slices.

## Step 1 

The inital data pipeline has been created by the System Admin. This initial slice is in the file `slice.json` and consists of:

* MQTT Broker - transport the data from data sources to analytics
* Analytics Client - a simple analytics client that does nothing
* BigQuery Dataset - the data sink for our data sources

Run the command `$ pizza create slice slice.json` to create the slice.

## Step 2

Once the System Admin informs the Data Consumer that the pipeline is ready and to search for data sources to add. 

The Data Consumer uses pizza to run a query for sensors, this JSON query is in the file `sensor_query.json`. Run the query
with the command `$ pizza provider query sensor_query.json`.

We assume the Data Consumer has chosen the temperature and humidity sensors.

## Step 3

Once the Data Consumer informs the System Admin what data sources he prefers, the System Admin updates the file `slice.json` with 
the data sources and required connectivities to the mqtt broker. 

We have the new resource and connectivities in the file `sensors.json`. Simply copy and paste the resources and connectivities
to the appropriate sections in the `slice.json` file.

Run the command `$ pizza update slice slice.json` to provision and configure the new data sources to the data pipeline. The 
data should now be available in the BigQuery table    



