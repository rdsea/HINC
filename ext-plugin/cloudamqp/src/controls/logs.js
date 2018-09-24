const axios = require('axios');
var cloudamqpplugin_config = require('config');
var config = cloudamqpplugin_config.get('cloudamqpadaptor');
function getLogs(resource){
    return new Promise((resolve, reject) => {
        resolve({
            status: 'SUCCESS',
            rawOutput: JSON.stringify({message: "This resource does not have any logs available"}),
            resourceUuid: resource.uuid,
        })
    })

}

module.exports.getLogs = getLogs;
