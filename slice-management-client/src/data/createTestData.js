const db = require('./db');

let provider1 = {
    "name":"mosquitto",
    "resources":null,
    "catalogue":[
        {
            "name": "broker",
            "pluginName": "mosquittobroker",
            "resourceType": "NETWORK_FUNCTION_SERVICE",
            "location": null,
            "metadata": {},
            "controlPoints": [],
            "dataPoints": [],
            "uuid": "xxxxxxx",
        }
    ],
    "managementPoints":[
       {
          "name":"provision mosquitto broker",
          "parameters":{
 
          },
          "controlType":"PROVISION",
          "accessPoints":[
             {
                "accessPointType":"HTTP",
                "uri":"http://localhost:3002/mosquittobroker/",
                "httpMethod":"POST"
             }
          ],
          "uuid":"provision mosquitto broker"
       }
    ],
    "uuid":"mosquitto",
    "_id":"p6EbJyzK1elnLa2U"
 }

 let provider2 = {
    "name":"sensor",
    "resources":null,
    "catalogue":[
        {
            "name": "sensor",
            "pluginName": null,
            "resourceCategory": "IOT_RESOURCE",
            "resourceType": "SENSOR",
            "location": null,
            "metadata": {
              "topic": "test",
            },
            "controlPoints": [],
            "dataPoints": [
              {
                "name": "humidity",
                "dataType": null,
                "unit": "percent"
              }
            ],
            "uuid": "xxxxxx",
        }
    ],
    "managementPoints":[
       {
          "name":"provision bts batteryCurrent sensor",
          "parameters":{
             "uri":"http://localhost:3000/"
          },
          "controlType":"PROVISION",
          "accessPoints":[
             {
                "accessPointType":"HTTP",
                "uri":"http://localhost:3001/sensor/bts/batteryCurrent",
                "httpMethod":"POST"
             }
          ],
          "uuid":"provision bts batteryCurrent sensor"
       },
       {
          "name":"provision bts batteryVoltage sensor",
          "parameters":{
             "uri":"http://localhost:3000/"
          },
          "controlType":"PROVISION",
          "accessPoints":[
             {
                "accessPointType":"HTTP",
                "uri":"http://localhost:3001/sensor/bts/batteryVoltage",
                "httpMethod":"POST"
             }
          ],
          "uuid":"provision bts batteryVoltage sensor"
       },
       {
          "name":"provision bts capacity sensor",
          "parameters":{
             "uri":"http://localhost:3000/",
             "topic":"topic"
          },
          "controlType":"PROVISION",
          "accessPoints":[
             {
                "accessPointType":"HTTP",
                "uri":"http://localhost:3001/sensor/bts/capacity",
                "httpMethod":"POST"
             }
          ],
          "uuid":"provision bts capacity sensor"
       },
       {
          "name":"provision bts generatorVoltage sensor",
          "parameters":{
             "uri":"http://localhost:3000/"
          },
          "controlType":"PROVISION",
          "accessPoints":[
             {
                "accessPointType":"HTTP",
                "uri":"http://localhost:3001/sensor/bts/generatorVoltage",
                "httpMethod":"POST"
             }
          ],
          "uuid":"provision bts generatorVoltage sensor"
       },
       {
          "name":"provision bts gridload sensor",
          "parameters":{
             "uri":"http://localhost:3000/"
          },
          "controlType":"PROVISION",
          "accessPoints":[
             {
                "accessPointType":"HTTP",
                "uri":"http://localhost:3001/sensor/bts/gridload",
                "httpMethod":"POST"
             }
          ],
          "uuid":"provision bts gridload sensor"
       },
       {
          "name":"provision bts humidity sensor",
          "parameters":{
             "uri":"http://localhost:3000/",
             "topic":"topic"
          },
          "controlType":"PROVISION",
          "accessPoints":[
             {
                "accessPointType":"HTTP",
                "uri":"http://localhost:3001/sensor/bts/humidity",
                "httpMethod":"POST"
             }
          ],
          "uuid":"provision bts humidity sensor"
       },
       {
          "name":"provision bts temperature sensor",
          "parameters":{
             "uri":"http://localhost:3000/",
             "topic":"topic"
          },
          "controlType":"PROVISION",
          "accessPoints":[
             {
                "accessPointType":"HTTP",
                "uri":"http://localhost:3001/sensor/bts/temperature",
                "httpMethod":"POST"
             }
          ],
          "uuid":"provision bts temperature sensor"
       }
    ],
    "uuid":"sensor",
    "_id":"0HV8xfylAHYdd40p"
 }

 db.providerDao().remove({}, {multi: true}).then(() => {
     return db.providerDao().insert(provider1);
 }).then(() => {
     return db.providerDao().insert(provider2);
 })

