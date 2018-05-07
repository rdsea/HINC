exports.demo = {
    "name": "js-artefact-runner",
    "pluginName": "js-artefact-plugin",
    "resourceType": "SOFTWARE_UNIT",
    "location": null,
    "metadata": {
        "software_artefact_ref": "softwareartefact1523451460864",
        "category": "datatransformer",
        "input": {
            "pattern":"pubsub",
            "protocol":"mqtt",
            "dataformat":"csv",
            "brokers": [
                {
                    "host": "35.205.250.115",
                    "port": 1883,
                    "clientId": "testclient1",
                    "username": "xxx",
                    "password": "xxx",
                    "topics": [
                        "topic_softwareartefact1523451460864"
                    ]
                }
            ]
        },
        "output": {
            "pattern":"pubsub",
            "protocol":"mqtt",
            "dataformat":"json",
            "brokers": [
                {
                    "host": "35.205.250.115",
                    "port": 1883,
                    "clientId": "testclient1",
                    "username": "xxx",
                    "password": "xxx",
                    "topics": [
                        "test"
                    ]
                }
            ]
        }

    },
    "controlPoints": [],
    "dataPoints": [],
    "uuid": "ingestionClient1523446947822"
}



exports.prototype = {
    metadata: {
        category:"datatransformer",
        datatransformer:{
            schema_in: "metadata schema in",
            schema_out: "metadata schema out"
        },
        input:[{
            pattern:"pubsub",
            protocol:"mqtt",
            dataformat: "csv",
            uri: "protocol://host:port",
            mqtt: {
                protocolspecific: "metadata",
                clientId: "testclient1",
                username: "xxx",
                password: "xxx",
                topics: ["topic1"]
            }}],
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