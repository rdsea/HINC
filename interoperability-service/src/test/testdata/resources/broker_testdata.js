exports.demo = {
    "name": "mosquitto broker",
    "pluginName": "mosquittobroker",
    "resourceType": "NETWORK_FUNCTION_SERVICE",
    "location": null,
    "metadata": {
        "category":"messagebroker",
        "protocol":"mqtt",
        "uri": "tcp://35.205.250.115:1883",
        "topics": ["test","topic_softwareartefact1523451460864"],
        "createdAt": 1523446948
    },
    "controlPoints": [],
    "dataPoints": [],
    "uuid": "broker1523446947822"
}



exports.prototype = {
    metadata: {
        category:"messagebroker",
        messagebroker:{
            categoryspecific: "metadata",
            protocol:"mqtt",
            dataformat: "csv",
            uri: "protocol://host:port",
            mqtt: {
                protocolspecific: "metadata",
                clientId: "testclient1",
                username: "xxx",
                password: "xxx",
                topics: ["topic1"]
            }
        }
    }
};