const axios = require('axios');
//const config = require('../../config')
var noderedplugin_config = require('config');
var config = noderedplugin_config.get('noderedadaptor');

function deleteResource(resource){
    console.log('making call to delete resource '+resource.uuid);
    return axios.delete(`${config.ENDPOINT}/datatransformer/${resource.uuid}`).then((res) => {
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
