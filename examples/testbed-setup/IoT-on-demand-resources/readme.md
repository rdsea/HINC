## Emulated gateway with HINC-enabled

This testbed include:

1. Virtual sensor
2. Virtual actuator
3. Emulated gateway via Docker
4. Dummy provider

Because of GitHub storage, you need to build HINC and TEIT and add artifacts in place.

First step, please build HINC and include following file

 - local-management-service-1.0.jar into current folder (same with gateway-start.sh)

To setup the virtual sensor:

1. Enter folder "teit-sensor"
2. Add your CSV file into the data/ folder.
3. Rename CSV file by: id.type.csv, for example: sensor1.temperature.csv
4. Copy following files:
   * EmulatedSensor-1.0-SNAPSHOT.jar into teit-sensor/
   * TEITPlugin-1.0.jar into teit-sensor/

To setup the virtual actuator:

1. Enter folder "teit-actuator"
2. Define your actuator as JSON format, put in data/ folder.
4. Copy following files
   * EmulatedActuator-1.0-SNAPSHOT.jar into teit-sensor/
   * TEITPlugin-1.0.jar into teit-sensor/


Run the gateway

1. Open the gateway-start.sh and edit the parameters.
2. Run with: $ /bin/bash gateway-start.sh
3. Docker can download about 450 MB to build images if need.
4. To clean the docker, run $ /bin/bash clean-docker.sh
5. If you run different gateways, please change the images name in gateway-start.sh


Some open datasets for testing:

 - http://datasensinglab.com/data/ (room temperature)

