#!/bin/bash

# install java 7 and tools
sudo apt-get update
sudo apt-get -y install openjdk-7-jre-headless wget nano unzip

# install openhab and its demo
mkdir openhab
cd openhab
wget https://bintray.com/artifact/download/openhab/bin/distribution-1.8.2-runtime.zip
unzip distribution-1.8.2-runtime.zip

wget https://bintray.com/artifact/download/openhab/bin/distribution-1.8.2-demo.zip
unzip distribution-1.8.2-demo.zip

nohup bash start.sh &
