/*
  Service template for google container engine
*/

var template = {
    "apiVersion": "v1",
    "kind": "Service",
    "metadata": {
      "name": "linkerd",
      "labels": {
        "app": "linkerd",
        "role": "iotcloudutils",
        "tier": "iotcloudutils"
      }
    },
    "spec": {
      "type": "LoadBalancer",
      "ports": [
        {
          "port": 9992,
          "targetPort": 9992,
          "name": "admin",
        },
      ], 
      "selector": {
        "app": "linkerd",
        "role": "iotcloudutils",
        "tier": "iotcloudutils"
      }
    }
  }

  module.exports = template;