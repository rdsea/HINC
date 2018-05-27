const axios = require('axios');
const config = require('../../config');


function provision(resource){
    let controlResult = null;

    console.log(`making http call with config: `);
    console.log(JSON.stringify(resource.metadata.parameters, null, '\t'));

    return axios.post(`${config.ENDPOINT}/storage/bigquery`, resource.metadata.parameters).then((res) => {
        let dataset = res.data;
        resource.uuid = dataset.datasetId;
        controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(resource),
            resourceUuid: dataset.datasetId,
        };
        console.log('successfuly control execution');
        console.log(JSON.stringify(controlResult, null, '\t'));
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

