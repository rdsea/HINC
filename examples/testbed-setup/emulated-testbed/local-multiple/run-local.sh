GW_START=$1
GW_END=$2

for i in "$@"
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
