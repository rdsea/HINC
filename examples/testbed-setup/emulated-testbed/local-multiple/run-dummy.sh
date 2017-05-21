SENSOR_NUM=500

if [ $# -gt 0 ]; then
  SENSOR_NUM=$1
fi

java -jar DummyProvider-1.0.jar 8888 $SENSOR_NUM


