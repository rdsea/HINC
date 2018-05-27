/*
  Deployment template for google container engine
*/

var template = {
    "apiVersion": "extensions/v1beta1",
    "kind": "Deployment",
    "metadata": {
      "name": "linkerd"
    },
    "spec": {
      "replicas": 1,
      "template": {
        "metadata": {
          "labels": {
            "app": "linkerd",
            "role": "iotcloudutils",
            "tier": "iotcloudutils"
          }
        },
        "spec": {
          "volumes":[],
          "containers": [
            {
              "name": "linkerd",
              "image": "linkerd/linkerd-tcp",
              "volumeMounts":[],
              "resources": {
                "requests": {
                  "cpu": "100m",
                  "memory": "250Mi"
                }
              },
              "ports": [
                {
                  "containerPort": 7474 
                },
                {
                  "containerPort": 9992 
                },
              ],
              "args": [
               "/io.buoyant/linkerd-tcp/config/linkerd-tcp.yml"
              ],

            }
          ]
        }
      }
    }
  }

  module.exports = template;
