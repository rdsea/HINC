wget -O MQTTSubscriber-1.0-SNAPSHOT.jar  https://bintray.com/leduchung/generic/download_file?file_path=MQTTSubscriber-1.0-SNAPSHOT.jar
wget -O local-management-service-1.0.jar https://bintray.com/leduchung/maven/download_file?file_path=at%2Fac%2Ftuwien%2Fdsg%2Fhinc%2Flocal-management-service%2F1.0%2Flocal-management-service-1.0.jar
wget -O TEITPlugin-1.0.jar https://bintray.com/leduchung/maven/download_file?file_path=at%2Fac%2Ftuwien%2Fdsg%2Fhinc%2FTEITPlugin%2F1.0%2FTEITPlugin-1.0.jar
cp TEITPlugin-1.0.jar teit-sensor
mv TEITPlugin-1.0.jar teit-actuator
wget https://github.com/EllaPham/TEIT/raw/artifacts/EmulatedSensor-1.0-SNAPSHOT.jar
mv EmulatedSensor-1.0-SNAPSHOT.jar teit-sensor
wget https://github.com/EllaPham/TEIT/raw/artifacts/EmulatedActuator-1.0-SNAPSHOT.jar
mv EmulatedActuator-1.0-SNAPSHOT.jar teit-actuator
