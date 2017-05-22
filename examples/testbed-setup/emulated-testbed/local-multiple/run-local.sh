#!/bin/bash

# Option 1: Set parameter below, and run: ./run-local.sh
# - Dummy provider generate sensors metadata (1 instances)
# - Several HINC local is run (many instances)
#
# Option 2: Run: ./run-local.sh GATEWAYS_NUM SENSORS_NUM
# For example: ./run-local.sh 5 50  (5 gateways, each simulate 50 sensors)

NUMBER_OF_GATEWAYS=5
NUMBER_OF_SENSORS=50

if [ $# -eq 2 ]; then
  NUMBER_OF_GATEWAYS=$1
  NUMBER_OF_SENSORS=$2
fi
mkdir -p localrun

# start dummy provider
nohup ./run-dummy.sh $NUMBER_OF_SENSORS &

# start HINC Local
for ((i=1; i<=NUMBER_OF_GATEWAYS; i++))
do
  path=localrun/local$i
  echo "running in $path"
  mkdir $path
  cp DummyProvider-1.0.jar $path
  cp local-management-service-1.0.jar $path
  cp hinc.conf $path
  cp sources.conf $path
  #cp -r hinc.db $path

  cd $path
  nohup java -cp "DummyProvider-1.0.jar:local-management-service-1.0.jar" sinc.hinc.local.Main &

  cd ../..
  sleep 2

done
echo "sleep 30 secs to load data...."
sleep 5
