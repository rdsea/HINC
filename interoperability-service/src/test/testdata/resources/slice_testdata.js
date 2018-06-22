exports.slice = {
    "resources": {
        "sensor":{
            "name": "sensor humidity",
            "pluginName": null,
            "resourceType": "IOT_RESOURCE",
            "location": null,
            "metadata": {
                "uri": "tcp://35.205.225.39:1883",
                "topic": "test",
                "createAt": 1523703608
            },
            "controlPoints": [],
            "dataPoints": [
                {
                    "name": "humidity",
                    "dataType": null,
                    "unit": "percent"
                }
            ],
            "uuid": "sensor1523703607915",

            "source": null,
            "target": "mqtt_connectivity"
        },

        "broker":{
            "name": "mosquitto broker",
            "pluginName": "mosquittobroker",
            "resourceType": "NETWORK_FUNCTION_SERVICE",
            "location": null,
            "metadata": {
                "category":"messagebroker",
                "protocol":"mqtt",
                "uri": "tcp://35.205.225.39:1883",
                "topics": ["test","topic_softwareartefact1523451460864"],
                "createdAt": 1523624322859
            },
            "controlPoints": [],
            "dataPoints": [],
            "uuid": "broker1523624322859",

            "source": "mqtt_connectivity",
            "target": null
        }
    }
    ,

    "connectivities": [
        {
            "connectivityId": "mqtt_connectivity",
            "accessPoint": {
                "accessPointType": "MQTT",
                "uri": "",
                "qos": 0,
                "topics":[]
            },
            "metadata":{}
        }
    ]
};