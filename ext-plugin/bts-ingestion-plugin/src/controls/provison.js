const axios = require('axios');
const config = require('../../config');
const randomString = require('randomstring');

function provision(resource){
    let controlResult = null;

    

    let provisionParameters = {
        data: resource.parameters.data,
        brokers:[],
        bigQuery: resource.parameters[resource.parameters.data],
    };

    resource.parameters.egressAccessPoints.forEach((accessPoint, index) => {
        provisionParameters.brokers.push({
            host: accessPoint.host,
            port: accessPoint.port,
            topics: accessPoint.topics,
            clientId: `${randomString.generate(5)}`,
            username: "xxx",
            password: "xxx"
        });
    })

    console.log(`making http call with config: `);
    console.log(JSON.stringify(provisionParameters, null, 2));

    return axios.post(`${config.ENDPOINT}/ingestionClient`, provisionParameters).then((res) => {
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
