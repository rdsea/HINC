exports.demo = {
    "name": "sensor humidity",
    "pluginName": "bts-sensor-provider",
    "resourceType": "IOT_RESOURCE",
    "location": null,
    "metadata": {
        "category":"sensor",
        "subcategory":"humidity",
        "output":{
            "pattern":"pubsub",
            "protocol":"mqtt",
            "dataformat":"csv",
            "uri": "tcp://35.205.250.115:1883",
            "topic": "topic_softwareartefact1523451460864",
            "createdAt": 1523446948
        }
    },
    "controlPoints": [],
    "dataPoints": [
        {
            "name": "humidity",
            "unit": "percent"
        }
    ],
    "uuid": "sensor1523446947822"
};

exports.prototype = {
    metadata: {
        category:"sensor",
        subcategory:"humidity",
        sensor:{
            categoryspecific: "metadata",
        },
        output:[{
            pattern:"pubsub",
            protocol:"mqtt",
            dataformat: "json",
            uri: "protocol://host:port",
            mqtt:{
                clientId: "testclient1",
                username: "xxx",
                password: "xxx",
                topics: ["topic1"]
            }}]
    }
};