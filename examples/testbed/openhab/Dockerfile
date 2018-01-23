FROM openjdk:8-jdk-alpine

RUN mkdir /openhab
WORKDIR /openhab

RUN apk update && apk add unzip

RUN wget https://bintray.com/artifact/download/openhab/bin/distribution-1.8.3-demo.zip 
RUN wget https://bintray.com/artifact/download/openhab/bin/distribution-1.8.3-runtime.zip

RUN unzip ./distribution-1.8.3-demo.zip
RUN unzip -o ./distribution-1.8.3-runtime.zip

CMD ["./start.sh"]
