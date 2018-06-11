const axios = require('axios');
const config = require('../../config');

function provision(resource){
    let controlResult =null;
    let firewall = null;

    return axios.post(`${config.ENDPOINT}/kubefw`, resource.parameters).then((res) => {
        firewall = res.data;
        resource.uuid = firewall.firewallId;
        console.log('successful control execution');

        let controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(resource),
            resourceUuid: resource.uuid,
        };

        return controlResult
    }).catch((err) => {
        console.log('control execution failed');
        console.error(err.response)
        controlResult = {
            status: 'FAILED',
            rawOutput: err.response,
        };
        return controlResult;
        throw err;
    });
}

module.exports.provision = provision;
