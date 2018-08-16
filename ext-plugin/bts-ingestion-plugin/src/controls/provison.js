const axios = require('axios');
const config = require('../../config');
const randomString = require('randomstring');

function provision(resource){
    let controlResult = null;

    
    let provisionParameters = null 
    if(resource.parameters.data){
        provisionParameters = {
            data: resource.parameters.data,
            brokers:[],
            bigQuery: resource.parameters[resource.parameters.data],
        };
    }else{
        provisionParameters = {
            data: "http",
            brokers:[],
        };
        resource.parameters.egressAccessPoints.forEach((accessPoint, index) => {
            if(accessPoint.applicationProtocol === "HTTP"){
                provisionParameters.http = {
                    uri: `http://${accessPoint.host}:${accessPoint.port}`
                }
            }
        })
    }
    

    resource.parameters.egressAccessPoints.forEach((accessPoint, index) => {
        if(accessPoint.applicationProtocol !== "MQTT") return;
        provisionParameters.brokers.push({
            host: accessPoint.host,
            port: accessPoint.port,
            topics: accessPoint.topics,
            clientId: `${randomString.generate(5)}`,
            username: accessPoint.username || "xxx",
            password: accessPoint.password || "xxx"
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
