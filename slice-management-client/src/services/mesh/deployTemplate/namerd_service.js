/*
  Service template for google container engine
*/

var template = {
    "apiVersion": "v1",
    "kind": "Service",
    "metadata": {
      "name": "namerd",
      "labels": {
        "app": "namerd",
        "role": "iotcloudutils",
        "tier": "iotcloudutils"
      }
    },
    "spec": {
      "type": "LoadBalancer",
      "ports": [
        {
          "port": 4180,
          "targetPort": 4180,
          "name": "main",
        },
        {
          "port": 4100,
          "targetPort": 4100,
          "name": "thrift",
        },
        {
          "port": 9991,
          "targetPort": 9991,
          "name": "admin", 
        }
      ], 
      "selector": {
        "app": "namerd",
        "role": "iotcloudutils",
        "tier": "iotcloudutils"
      }
    }
  }

  module.exports = template;