const prompt = require('inquirer').createPromptModule();
const request = require('request');
const config = require("../../config")
const path = require('path');

exports.command = 'suite <name>'
exports.desc = ''
exports.builder = {}

exports.handler = function (argv) {
    const metadatacounts = [1,2,3,4,5,6,7,8,9,10,20,30,40,50,60,70,80,90, 100,200,300,400,500,600,700,800,900, 1000];
    //let nodecounts = [1000];
    //const nodecounts = [1,3];
    let nodecounts = [1];
    //const metadatacounts = [1,2,3,5,7,10, 20,30,50,70,100];
    let iterations = 5;
    let instance_name = "fixed_"+argv.name;


    if(argv.name === "node1000"){
        nodecounts = [1000];


    }else if(argv.name === "node100"){
        nodecounts = [100];

    }else if(argv.name === "node10"){
        nodecounts = [10];

    }else if(argv.name === "node1"){
        nodecounts = [1];
    }else{
        console.log("invalid instance name");
        return;
    }



    //let jsonObj = {slice:s}
    let requestBody = { json: true, body: {node_counts:nodecounts, num:iterations, metadata_counts:metadatacounts, instance_name:instance_name} };
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