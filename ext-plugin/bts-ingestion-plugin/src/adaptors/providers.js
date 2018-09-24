const axios = require('axios');
//const config = require('../../config');
var ingestplugin_config = require('config');
var config = ingestplugin_config.get('ingestionadaptor');

/**
 * gets the available resources provider information
 */
function getProvider(settings){
    return axios.get(`${config.ENDPOINT}/ingestionClient`).then((res) => {
        let sampleConfig = res.data.sampleConfiguration;

        let availableResources = [];
        availableResources.push({
            plugin: 'btsingestion',
            providerUuid: config.ADAPTOR_NAME,
            resourceType: 'CLOUD_SERVICE',
            name: `bts ingestion client`,
            controlPoints: [],
            dataPoints: [],
            type: 'SOFTWARE_UNIT',
            location: null,
            parameters:{
                egressAccessPoints: [{
                    applicationProtocol: "MQTT",
                    host: "broker host",
                    port: "broker port",
                    username: "xxx",
                    password: "xxx",
                    accessPattern: "PUBSUB",
                    networkProtocol: "IP",
                    qos: 0,
                    topics: ["topic1", "topic2"]
                }],
                ingressAccessPoints: [],
                "data": "bigQuery",
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
            },
            metadata: {

            },
        });

        availableResources.push({
            plugin: 'btsingestion',
            providerUuid: config.ADAPTOR_NAME,
            resourceType: 'CLOUD_SERVICE',
            resourceCategory: 'SOFTWARE_ARTIFACT',
            name: `bts ingestion client`,
            controlPoints: [],
            dataPoints: [],
            location: null,
            parameters:{
                egressAccessPoints: [
                    {
                        applicationProtocol: "MQTT",
                        host: "broker host",
                        port: "broker port",
                        accessPattern: "PUBSUB",
                        networkProtocol: "IP",
                        qos: 0,
                        topics: ["topic1", "topic2"]
                    },
                    {
                        applicationProtocol: "HTTP",
                        host: "localhost",
                        port: "3000",
                        accessPattern: "",
                        networkProtocol: "IP",
                    },

                ],
                ingressAccessPoints: [],
            },
            metadata: {

            },
        })

        let provider = {
            name: config.ADAPTOR_NAME,
            uuid: config.ADAPTOR_NAME,
            availableResources: availableResources,
        };

        return provider;
    });
}



module.exports.getProvider = getProvider;
