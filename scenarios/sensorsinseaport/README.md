# Sensors in SeaPort

basic slice:

modified slice
## Configuration

Make sure you have global running 

Make Local running

Make sure you have the following providers configured with Local:

- bts-sensor provider
- mqtt broker provider
- bigquery provider
- ingest provider
## Provisioning the basic slice

$node pizza slice_create [basic_slice]

you will see the slice created.

## Check and monitoring the basic slice

$node pizza slice [name of slice]

$node pizza slice logs [slice name]

## Check if the the slice is running

you want to see if the data is really analyzed and stored based on the slice

so you can open bigquery and see 

## Let play a bit

kill sensor

$pizza get slice

No data comming

you know that the sensor fails you can restart

$pizza ...restart_sensor.json

## New Requirements

see the new resources for the new slice:

$more [new slice]

Provisioning

$pizza slice update [updated slice]

### Search for interoperability


### See the new slice in action


