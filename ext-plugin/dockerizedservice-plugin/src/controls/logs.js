const axios = require('axios');

function getLogs(resource){
    return new Promise((resolve, reject) => {
        resolve({
            status: 'SUCCESS',
            rawOutput: JSON.stringify({message: "Not implemented"}),
            resourceUuid: resource.uuid,
        })
    })

}

module.exports.getLogs = getLogs;
