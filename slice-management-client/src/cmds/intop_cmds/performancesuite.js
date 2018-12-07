const prompt = require('inquirer').createPromptModule();
const request = require('request');
const config = require("../../config")
const path = require('path');

exports.command = 'suite'
exports.desc = ''
exports.builder = {}

exports.handler = function (argv) {

    //let jsonObj = {slice:s}
    let requestBody = { json: true, body: {} };
    let requestUri = config.intop_service_uri + "/performance/suite/";

    request.post(requestUri, requestBody , (err, res, body) => {
        if (err) {
            console.err("Error connecting to the interoperability service. Please check the http uri of the interoperability service" +
                "(" + config.intop_service_uri + "). Detailed error message:")
            return console.err(err);
        }

        console.log(body);


    });
};