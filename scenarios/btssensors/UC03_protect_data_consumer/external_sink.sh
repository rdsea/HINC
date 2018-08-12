#!/bin/bash

function ctrl_c() {
    echo 'deleting and cleaning up'
}

kubectl run external-client --image rdsea/echo-server --port=3000 
kubectl expose deployment external-client --type=LoadBalancer
sleep 5
kubectl logs -f $(kubectl get pods -l run=external-client -o jsonpath={$.items[0].metadata.name})

trap ctrl_c INT

echo 'deleting and cleaning up'
kubectl delete service external-client
kubectl delete deployment external-client


