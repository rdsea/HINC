var template = {
    "admin": {
      "ip": "0.0.0.0",
      "port": 9992,
      "metricsIntervalSecs": 5
    },
    "routers": [
      {
        "label": "resource",
        "servers": [

        ],
        "interpreter": {
          "kind": "io.l5d.namerd.http",
          "baseUrl": "http://172.17.0.1:4180",
          "namespace": "default",
          "periodSecs": 20
        }
      }
    ]
  }

  module.exports = template;