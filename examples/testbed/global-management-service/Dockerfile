FROM rdsea/hinc-base

WORKDIR ./global-management-service
RUN mvn clean install

WORKDIR ./target

RUN echo "BROKER=amqp://guest:guest@amqp" >> hinc.conf
RUN echo "BROKER_TYPE=amqp" >> hinc.conf
RUN echo "GROUP=test" >> hinc.conf

CMD ["java", "-jar", "global-management-service-1.0-war-exec.jar", "-httpPort", "9000"]
