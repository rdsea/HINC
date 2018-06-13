#!/bin/bash

docker build -t rdsea/bigquery-adaptor .
docker push rdsea/bigquery-adaptor
