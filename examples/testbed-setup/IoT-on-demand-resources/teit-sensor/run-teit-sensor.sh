WORKING_DIR=/tmp/teit-sensor
CURRENT_DIR=`pwd`
echo "Current dir: $CURRENT_DIR"

mkdir -p $WORKING_DIR
for file in $(ls ./data/*.csv)
do
  idAndType=`basename $file .csv`
  sensorDir=$WORKING_DIR/$idAndType
  mkdir -p $sensorDir
  echo "Bootstrap sensor in: $sensorDir"
  cp ./EmulatedSensor-1.0-SNAPSHOT.jar $sensorDir
  cp ./sensor.conf $sensorDir
  cp ./sensor.sh $sensorDir
  cp ./data/$idAndType.csv $sensorDir
  cp ./data/$idAndType.meta $sensorDir  

  sed -i 's#sensorID=.*#sensorID='${idAndType%.*}'#'  $sensorDir/sensor.conf
  sed -i 's#sensorType=.*#sensorType='${idAndType##*.}'#'  $sensorDir/sensor.conf
  sed -i 's#data.csv.fileName.*#data.csv.fileName='$idAndType'.csv#'  $sensorDir/sensor.conf
  cd $sensorDir
  bash ./sensor.sh start
  cd "$CURRENT_DIR"
  echo "Back to folder: $CURRENT_DIR"
  echo "-----"
done

