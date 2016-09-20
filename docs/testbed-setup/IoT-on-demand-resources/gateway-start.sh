#!/bin/bash

### VARIABLES -- CHANGE FOR YOURSELF ###
DOCKER_IMAGE_NAME=demo:actuator
DOCKER_IMAGE_BASE=leduchung/ubuntu:14.04-jre8-mosquitto

HINC_ENABLE=true
#HINC_QUEUE=amqp://uluzkylq:jk8ETV-nwCRek6dm5KurNztmlEot4uSs@bunny.cloudamqp.com/uluzkylq
HINC_QUEUE=amqp://100.96.9.10
HINC_GROUP=ericsson2016
DATA_FORWARD=tcp://100.96.9.10:1883
AUTO_LOCATION=true

TEIT_SENSOR_ENABLE=false
TEIT_ACTUATOR_ENABLE=true

DUMMY_PROVIDER_ENABLE=false


### MODULE CONFIGURATION ###
function notifyCreated {  
  echo "Created file: $1"
  echo "----------------"
  cat $1
  echo "----------------"
  echo "  "
}

function enableDummy {
  echo "We do not support DUMMY PROVIDER at this time."
}

function enableTeitActuator {
	TEIT_SETUP=teit-actuator
	echo "Adding TEIT actuators into Dockerfile...."
	echo -e "\n # add TEIT related artifacts and configuration" >> Dockerfile
	echo -e "RUN mkdir -p $HINC_SETUP/$TEIT_SETUP " >> Dockerfile
	echo -e "ADD ./$TEIT_SETUP/deploy-actuators.sh $HINC_SETUP/$TEIT_SETUP/deploy-actuators.sh" >> Dockerfile
	echo -e "ADD ./$TEIT_SETUP/EmulatedActuator-1.0-SNAPSHOT.jar $HINC_SETUP/$TEIT_SETUP/EmulatedActuator-1.0-SNAPSHOT.jar" >> Dockerfile
	echo -e "ADD ./$TEIT_SETUP/TEITPlugin-1.0.jar $HINC_SETUP/TEITPlugin-1.0.jar " >> Dockerfile
	echo -e "ADD ./$TEIT_SETUP/actuator.sh $HINC_SETUP/$TEIT_SETUP/actuator.sh " >> Dockerfile
	echo -e "RUN mkdir -p $HINC_SETUP/$TEIT_SETUP/data " >> Dockerfile
	for file in $(ls $TEIT_SETUP/data)
    do
      basename=`basename $file`
      echo -e "ADD ./$TEIT_SETUP/data/$file $HINC_SETUP/$TEIT_SETUP/data/$basename " >> Dockerfile
    done
    BOOTSTRAP_SCRIPT=$BOOTSTRAP_SCRIPT" \ncd $HINC_SETUP/$TEIT_SETUP  \n/bin/bash deploy-actuators.sh"
    JAR_LIST=$JAR_LIST":TEITPlugin-1.0.jar"
}

function enableTeitSensor {
      TEIT_SETUP=teit-sensor
      echo "Adding TEIT artifacts into Dockerfile...."
      echo -e "\n # add TEIT related artifacts and configuration" >> Dockerfile
      echo -e "RUN mkdir -p $HINC_SETUP/$TEIT_SETUP " >> Dockerfile
      echo -e "ADD ./$TEIT_SETUP/run-teit-sensor.sh $HINC_SETUP/$TEIT_SETUP/run-teit-sensor.sh " >> Dockerfile
      echo -e "ADD ./$TEIT_SETUP/sensor.conf $HINC_SETUP/$TEIT_SETUP/sensor.conf " >> Dockerfile
      echo -e "ADD ./$TEIT_SETUP/sensor.sh $HINC_SETUP/$TEIT_SETUP/sensor.sh " >> Dockerfile
      echo -e "ADD ./$TEIT_SETUP/EmulatedSensor-1.0-SNAPSHOT.jar $HINC_SETUP/$TEIT_SETUP/EmulatedSensor-1.0-SNAPSHOT.jar " >> Dockerfile
      echo -e "ADD ./$TEIT_SETUP/TEITPlugin-1.0.jar $HINC_SETUP/TEITPlugin-1.0.jar " >> Dockerfile
      
      for file in $(ls $TEIT_SETUP/data)
      do
        basename=`basename $file`
        echo -e "ADD ./$TEIT_SETUP/data/$file $HINC_SETUP/$TEIT_SETUP/data/$basename " >> Dockerfile
      done
      BOOTSTRAP_SCRIPT=$BOOTSTRAP_SCRIPT" \ncd $HINC_SETUP/$TEIT_SETUP  \n/bin/bash run-teit-sensor.sh"
      JAR_LIST=$JAR_LIST":TEITPlugin-1.0.jar"
}


#### BUILD DOCKER IMAGE ####
if [ ! -z $(docker images -q $DOCKER_IMAGE_NAME) ]; then
    echo "Docker image: $DOCKER_IMAGE_NAME exists.. No need to build.. If you want to rebuild the image, please rename the DOCKER_IMAGE_NAME"
else
    HINC_SETUP=/tmp/hinc-artifact 
    
    echo "Docker image is not found... Building a new one..."
    
    echo -e "FROM $DOCKER_IMAGE_BASE" > Dockerfile
    echo -e "MAINTAINER Ericsson " >> Dockerfile
    echo -e "RUN mkdir -p $HINC_SETUP " >> Dockerfile
    
    ## install mosquitto for MQTT
    #echo -e "RUN sudo apt-get update" >> Dockerfile
    #echo -e "RUN sudo apt-get install -y mosquitto" >> Dockerfile
    
    
    ## initiate for bootstrap script and HINC info source
    BOOTSTRAP_SCRIPT="#!/bin/bash \n mosquitto -d"
    JAR_LIST="local-management-service-1.0.jar"
    
        
    ### add enabled components
    if [ $DUMMY_PROVIDER_ENABLE = "true" ]; then
      enableDummy
    fi
        
    if [ $TEIT_SENSOR_ENABLE = "true" ]; then
      enableTeitSensor
    fi

    if [ $TEIT_ACTUATOR_ENABLE = "true" ]; then
      enableTeitActuator
    fi

    ### adding components is done
    
    ## finalize infosource and bootstrap script. Add to docker
    echo -e "\n# add HINC configuration file and bootstrap script" >> Dockerfile
    echo -e "ADD ./sources.conf $HINC_SETUP/sources.conf" >> Dockerfile
    notifyCreated sources.conf
    
    # create and add hinc.conf
    echo -e "BROKER=$HINC_QUEUE " > hinc.conf
    echo -e "BROKER_TYPE=amqp " >> hinc.conf
    echo -e "GROUP=$HINC_GROUP " >> hinc.conf
    echo -e "DATA_FORWARD=$DATA_FORWARD" >> hinc.conf
    echo -e "AUTO_LOCATION=$AUTO_LOCATION" >> hinc.conf
    notifyCreated hinc.conf

    echo -e "ADD ./hinc.conf $HINC_SETUP/hinc.conf " >> Dockerfile
    
    # build the gateway-bootstrap script
    echo -e "$BOOTSTRAP_SCRIPT" > gateway-bootstrap.sh
    echo -e "cd $HINC_SETUP" >> gateway-bootstrap.sh
    echo -e 'java -cp "'$JAR_LIST'" sinc.hinc.local.Main' >> gateway-bootstrap.sh
    notifyCreated gateway-bootstrap.sh
    
    echo -e "ADD ./local-management-service-1.0.jar $HINC_SETUP/local-management-service-1.0.jar" >> Dockerfile
    echo -e "ADD ./gateway-bootstrap.sh $HINC_SETUP/gateway-bootstrap.sh" >> Dockerfile
    echo -e "ADD ./MQTTSubscriber-1.0-SNAPSHOT.jar $HINC_SETUP/MQTTSubscriber-1.0-SNAPSHOT.jar" >> Dockerfile
    
    echo -e "\nCMD [\"/bin/sh\", \"$HINC_SETUP/gateway-bootstrap.sh\"]" >> Dockerfile  
    notifyCreated Dockerfile  
    
    # build the image
    echo -e "Start to build the docker image: $DOCKER_IMAGE_NAME..."
    docker build -t $DOCKER_IMAGE_NAME .
fi

### SPIN UP CONTAINER ###
echo -e "Spinning up docker container..."
docker run $DOCKER_IMAGE_NAME
echo -e "Done! THe emulated HINC-enabled gateway terminated!"





