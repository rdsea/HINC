/*
  Deployment template for google container engine
*/

var template = {
    "apiVersion": "extensions/v1beta1",
    "kind": "Deployment",
    "metadata": {
      "name": "namerd"
    },
    "spec": {
      "replicas": 1,
      "template": {
        "metadata": {
          "labels": {
            "app": "namerd",
            "role": "iotcloudutils",
            "tier": "iotcloudutils"
          }
        },
        "spec": {
          "volumes":[],
          "containers": [
            {
              "name": "namerd",
              "image": "buoyantio/namerd",
              "volumeMounts":[],
              "resources": {
                "requests": {
                  "cpu": "100m",
                  "memory": "250Mi"
                }
              },
              "ports": [
                {
                  "containerPort": 4180 
                },
                {
                  "containerPort": 4100 
                },
                {
                  "containerPort": 9991 
                }
              ],
              "args": [
               
              ],

            }
          ]
        }
      }
    }
  }

  module.exports = template;
