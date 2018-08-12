# Use Case - Remove Pipeline

A Data Consumer no longer wishes to use the system and wishes to 
remove his data pipeline

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
* BTS temperature sensors - temperature datasource
* BTS humidity sensors - humidity datasource

Run the command `$ pizza create slice slice.json` to create the slice.

## Step 2

The pipeline can be deleted simply using the `sliceId` found in `slice.json`. Simply
run `$ pizza slice delete sliceuc9` to delete the slice.




