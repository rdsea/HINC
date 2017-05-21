export BASE=`dirname $0`
cd $BASE

java -jar EmulatedActuator-1.0-SNAPSHOT.jar "$@"
