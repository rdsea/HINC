const axios = require('axios');
var cloudamqpplugin_config = require('config');
var config = cloudamqpplugin_config.get('cloudamqpadaptor');
function deleteResource(resource){
    console.log('making call to delete resource '+resource.uuid);
    return axios.delete(`${config.ENDPOINT}/${resource.uuid}`, {auth: { username: process.env.AMQP_KEY}}).then((res) => {
        controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(resource),
            resourceUuid: resource.uuid,
        };
        console.log('successfuly deleted resource '+resource.uuid);
        console.log(JSON.stringify(controlResult, null, '\t'));
        return controlResult;
    }).catch((err) => {
        console.log('control execution failed');
        console.error(err.toString());
        controlResult = {
            status: 'FAILED',
            rawOutput: "",
        };

        console.log(controlResult);
        return controlResult;
    });
}

module.exports.deleteResource = deleteResource;
