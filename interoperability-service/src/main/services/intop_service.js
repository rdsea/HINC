const reqprom = require('request-promise');
const config = require("../config");

let baseUri = config.intop_service_uri + "/interoperability";

module.exports = {
    recommendation:_recommendation,
    check:_check
}

function _recommendation(slice, timeout){
    let options = {
        method: 'POST',
        uri: baseUri + "/recommendation",
        body: slice,
        timeout:timeout,
        json: true // Automatically stringifies the body to JSON
    };
    return reqprom(options);
}

function _check(slice, timeout){
    let options = {
        method: 'POST',
        uri: baseUri + "/check",
        body: slice,
        timeout: timeout,
        json: true // Automatically stringifies the body to JSON
    };
    return reqprom(options);
}