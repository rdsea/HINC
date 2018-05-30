const axios = require('axios');
const config = require('../../config');


/**
 * gets the available resources provider information
 */
function getProvider(settings){
    return axios.get(`${config.ENDPOINT}/storage/bigquery`).then((res) => {
        let sampleConfig = res.data.sampleConfig;

        let availableResources = [];
        availableResources.push({
            plugin: 'bigquery',
            resourceType: 'CLOUD_SERVICE',
            name: `bigQuery dataset`,
            controlPoints: [],
            dataPoints: [],
            type: 'SOFTWARE_UNIT',
            location: null,
            metadata: {
                parameters:  {
                    "datasetId": "datasetId",
                    "tables": [
                        {
                            "id": "tableId",
                            "schema": [
                                {
                                    "description": "field description",
                                    "mode": "REQUIRED/",
                                    "name": "fieldname",
                                    "type": "INT64/FLOAT64/STRING/BOOL/BYTES/DATE/DATETIME/TIME/TIMESTAMP"
                                }
                            ]
                        }
                    ]
                },
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