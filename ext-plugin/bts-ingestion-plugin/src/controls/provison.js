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
            rawOutput: JSON.stringify(resource),
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



//provision(resource).catch((err) => console.log(err));
