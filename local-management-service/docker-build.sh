#!/bin/bash

docker build -t rdsea/local .
docker push rdsea/local
