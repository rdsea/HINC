WORKING_DIR=/tmp/teit-actuator
CURRENT_DIR=`pwd`
echo "Current dir: $CURRENT_DIR"

mkdir -p $WORKING_DIR
for file in $(ls ./data/*.json)
do
  idAndType=`basename $file .json`
  actuatorDir=$WORKING_DIR/$idAndType
  mkdir -p $actuatorDir
  echo "Bootstrap sensor in: $actuatorDir"
  cp ./EmulatedActuator-1.0-SNAPSHOT.jar $actuatorDir
  cp ./actuator.sh $actuatorDir
  cp ./data/$idAndType.json $actuatorDir/actuator.json

done

