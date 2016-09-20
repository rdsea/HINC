export BASE=`dirname $0`
cd $BASE

listCommand() {

        echo "  start"
        echo "  stop"
        echo "  change-rate <rate>"
        echo "  connect-console"
        echo "  connect-mqtt <broker> [topic]"
}

if [ $# -lt 1 ]
then
        echo "Usage : $0 <command> start|stop|change-rate|connect-console|connect-mqtt"
        echo "Command:"
        listCommand
        exit
fi

case "$1" in
 list-commands)
   listCommand
 ;;
 start)
   if [ -f $BASE/sensor.pid ]
   then
     PID=`cat sensor.pid`
     if ps -p "$PID" > /dev/null
     then
       echo "Sensor started already. PID: $PID"
       exit 1
     fi
   fi
 
   java -jar EmulatedSensor-1.0-SNAPSHOT.jar  > sensor.out 2>&1 &
   pid=$!
   if [ ! -z "$pid" ]
   then
     echo "Sensor started. PID: $pid"
     echo $pid > sensor.pid
   else
     echo "Failed to start sensor. Please check sensor.log for more detail."
   fi
   
   ;;
   
 stop)
   if [ ! -f $BASE/sensor.pid ]
   then
     echo "Sensor seems to be stopped, nothing to do."
     exit 1
   fi
   kill `cat $BASE/sensor.pid`
   rm $BASE/sensor.pid
   echo "Sensor stopped sucessfully!"
   ;;
   
 change-rate)
   if [ -n "$2" -a "$2" != " " ]
   then
     NEW_VAL=$2
     sed -i 's#rate=.*#rate='$NEW_VAL'#' $BASE/sensor.conf 
     echo "Update rate set to $NEW_VAL mili-seconds!"
     touch sensor.conf
   else
     echo "No update rate provided!"
     exit 1
   fi
   ;;
   
 connect-console)
   sed -i 's#platform=.*#platform=teit.sensor.PlatformConsole.ConsolePlatform#' $BASE/sensor.conf
   touch sensor.conf   
   ;;
   
 connect-mqtt)
   sed -i 's#platform=.*#platform=teit.sensor.MQTT.MQTTOutput#' $BASE/sensor.conf
   sed -i 's#platform.mqtt.url=.*#platform.mqtt.url='$2'#' $BASE/sensor.conf
   if [ ! -z "$3" ]; then
     sed -i 's#platform.mqtt.topic=.*#platform.mqtt.topic='$3'#' $BASE/sensor.conf
   fi
   touch sensor.conf
   ;;
   
esac
