const axios = require('axios');
const config = require('../../config')

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