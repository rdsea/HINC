FROM openjdk:8-jdk-alpine

COPY . /HINC
WORKDIR HINC
RUN apk update && apk add maven
RUN mvn clean install

WORKDIR ./common
RUN mvn clean install

WORKDIR ../communication
RUN mvn clean install

WORKDIR ../model
RUN mvn clean install

WORKDIR ../collector
RUN mvn clean install

WORKDIR ../repository
RUN mvn clean install

WORKDIR ../global-management-service
RUN mvn clean install

WORKDIR ../local-management-service
RUN mvn clean install

WORKDIR /HINC
