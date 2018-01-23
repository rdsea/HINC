FROM rdsea/hinc-base

WORKDIR ./local-management-service
RUN mvn clean install

WORKDIR ./target

RUN echo "BROKER=amqp://guest:guest@amqp" >> hinc.conf
RUN echo "BROKER_TYPE=amqp" >> hinc.conf
RUN echo "GROUP=test" >> hinc.conf

RUN echo "global.interval=0" >> sources.conf
RUN echo "global.enable=openhab" >> sources.conf
RUN echo "openhab.endpoint=http://openhab:8080/rest" >> sources.conf

WORKDIR /HINC/ext-plugin/TransformOpenHAB
RUN mvn clean install

WORKDIR /HINC/local-management-service/target
RUN cp /HINC/ext-plugin/TransformOpenHAB/target/TransformOpenHAB-1.0.jar .


CMD ["java", "-cp", "local-management-service-1.0.jar:TransformOpenHAB-1.0.jar", "sinc.hinc.local.Main"]
