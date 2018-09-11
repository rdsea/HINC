#!/bin/bash
# default binary repository
gsdest="gs://rsihub-deployment-artifacts" 
if [ $# -gt 1 ]; 
    then gsdest=$2
fi
echo "Copy $1 to $gsdest"
gsutil cp -r $1 $gsdest
