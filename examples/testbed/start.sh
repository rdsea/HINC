#! /bin/sh

docker-compose up -d amqp
sleep 5

docker-compose up -d local-management-service
sleep 5

docker-compose up -d global-management-service
sleep 5

echo "all components up and running"
