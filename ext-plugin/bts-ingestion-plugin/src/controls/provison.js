const axios = require('axios');
const config = require('../../config');

function provision(resource){
    let controlResult = null;

    console.log(`making http call with config: `);
    resource.metadata.parameters.brokers[0].host = resource.metadata._proxy.ip;
    resource.metadata.parameters.brokers[0].port = resource.metadata._proxy.port;
    console.log(JSON.stringify(resource.metadata.parameters, null, 2));

    return axios.post(`${config.ENDPOINT}/ingestionClient`, resource.metadata.parameters).then((res) => {
        let ingestionClient = res.data;
        resource.uuid = ingestionClient.ingestionClientId;
        controlResult = {
            status: 'SUCCESS',
            rawOutput: resource,
            resourceUuid: ingestionClient.ingestionClientId,
        };
        console.log('successfuly control execution');
        console.log(JSON.stringify(controlResult, null, 2));
        return controlResult;
    }).catch((err) => {
        console.log('control execution failed');
        console.error(err.response)
        controlResult = {
            status: 'FAILED',
            rawOutput: err.response,
        }; 
        return controlResult;
    });
}

module.exports.provision = provision;

let resource = {
    "name": "bts ingestion client",
    "pluginName": "btsingestion",
    "providerUuid": "ingest",
    "resourceType": "SOFTWARE_ARTIFACT",
    "location": null,
    "metadata": {
        "_proxy":{
            "ip": "127.0.0.1",
            "port": 7474
        },
        "parameters":{
            "data": "bigQuery",
            "brokers": [
                    {
                            "host": "35.205.138.49",
                            "port": 1883,
                            "clientId": "testclient1",
                            "username": "xxx",
                            "password": "xxx",
                            "topics": [
                                    "test"
                            ]
                    }
            ],
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
            },
    }
    
    }
}


//provision(resource).catch((err) => console.log(err));
