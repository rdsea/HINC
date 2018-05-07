exports.demo = {
        "name": "bts ingestion client",
        "pluginName": "btsingestion",
        "resourceType": "SOFTWARE_UNIT",
        "location": null,
        "metadata": {
            "category":"ingestion",
            "data": "bigQuery",
            "input":{
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
            },
            "bigQuery": {
                "dataset": "testDataset",
                "tables": [
                    {
                        "id": "testTable",
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
};

exports.prototype = {
    metadata: {
        category:"ingestion",
        ingestion:{
            categoryspecific: "metadata",
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
            }}]
    }
};