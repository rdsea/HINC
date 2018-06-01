#!/bin/bash

docker build -t rdsea/ingestion-adaptor .
docker push rdsea/ingestion-adaptor
