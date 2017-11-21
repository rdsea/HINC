# TEIT plugin

This package integrate information from [TEIT](https://ellapham.github.io/TEIT/) project. TEIT is a set of small tools to setup IoT testbed.

This package requires settings in source.conf:

```
### The working dir of the sensor
### The adaptor search for sensor.conf file that provide sensor information.
teit.workingdir=/tmp/teit-sensor

### The working dir of the actuator
### This adaptor will search for *.json file that describe the actuator.
teit-actuator.workingdir=/tmp/teit-actuator
```