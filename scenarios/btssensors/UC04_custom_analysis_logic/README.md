# Use Case - New Data Consumer

The default data processing logic of the system is not adapted
to the Data Consumer's needs. The Data Consumer consults with the Data Scientist to 
implement a custom data processing logic for the data pipeline.

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

Once the Data Scientist receives a request for custom analysis logic, he deploys a NodeRED instance, to create a custom data flow.
Paste the resource in `nodered.json` into the slice and run `$ pizza slice update slice.json`. 

Once the instance has been deployed you can check its host and port in the `slice.json` under `parameters.ingressAccessPoints`. The
UI can be reached by using the `http://<host>:<port>` URL in your browser.

## Step 3

A `shortcut.js` and `nodred_flow.json` files have been created to speed up the data flow creation process. The 
data flow consumes from the sensors through MQTT and multiplies the values by an offset of 100. The resulting
dataset is sent to the analytics client through the topic `<existing_topic>_custom`. 

The `shortcut.js` script configures the correct broker URL in the NodeRED flow and also reconfigures the `slice.json` file
so that the analytics client consumes from the new topic.

* execute `$ node shortcut.js`
* In the NodeRED UI, import the contents o `nodered_flow.json`
* `$ pizza slice update slice.json`

The resulting data stored into the BigQuery datasink will be the new values processed by the NodeRED dataflow.



