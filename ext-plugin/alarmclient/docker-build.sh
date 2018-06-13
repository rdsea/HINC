#!/bin/bash

docker build -t rdsea/alarmclient-adaptor .
docker push rdsea/alarmclient-adaptor
