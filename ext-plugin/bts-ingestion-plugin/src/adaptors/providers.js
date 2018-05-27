const axios = require('axios');
const config = require('../../config');


/**
 * gets the available resources provider information
 */
function getProvider(settings){
    return axios.get(`${config.ENDPOINT}/ingestionClient`).then((res) => {
        let sampleConfig = res.data.sampleConfiguration;

        let availableResources = [];
        availableResources.push({
            plugin: 'btsingestion',
            resourceType: 'CLOUD_SERVICE',
            name: `bts ingestion client`,
            controlPoints: [],
            dataPoints: [],
            type: 'SOFTWARE_UNIT',
            location: null,
            metadata: {
                paramters:{
                    "url": "/ingestionClient/",
                    "sampleConfiguration": {
                        "data": "bigQuery",
                        "brokers": [
                            {
                                "host": "localhost",
                                "port": 1883,
                                "clientId": "testclient1",
                                "username": "xxx",
                                "password": "xxx",
                                "topics": [
                                    "test1",
                                    "test2"
                                ]
                            },
                            {
                                "host": "localhost",
                                "port": 1883,
                                "clientId": "testclient2",
                                "topics": [
                                    "test3",
                                    "test4"
                                ]
                            }
                        ],
                        "bigQuery": {
                            "dataset": "datasetId",
                            "tables": [
                                {
                                    "id": "tableId",
                                    "topics": [
                                        "topic1",
                                        "topic2"
                                    ]
                                }
                            ]
                        }
                    }
                },
            },
        });

        let provider = {
            name: config.ADAPTOR_NAME,
            uuid: config.ADAPTOR_NAME,
            availableResources: availableResources,
        };

        return provider;
    });
}

module.exports.getProvider = getProvider;
