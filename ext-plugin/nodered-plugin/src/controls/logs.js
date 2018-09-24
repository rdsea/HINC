const axios = require('axios');
//const config = require('../../config')
var noderedplugin_config = require('config');
var config = noderedplugin_config.get('noderedadaptor');
function getLogs(resource){
    return axios.get(`${config.ENDPOINT}/mosquittobroker/${resource.uuid}/logs`).then((res) => {
        controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(res.data),
            resourceUuid: resource.uuid,
        };
        console.log('successfuly fetched logs for resource '+resource.uuid);
        console.log(JSON.stringify(controlResult, null, '\t'));
        return controlResult;
    }).catch((err) => {
        console.log('control execution failed');
        console.error(err)
        controlResult = {
            status: 'FAILED',
            rawOutput: err,
        };
        return controlResult;
    });
}

module.exports.getLogs = getLogs;
