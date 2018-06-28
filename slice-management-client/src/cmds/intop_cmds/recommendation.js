const request = require('request');
const config = require("../../config");
const path = require("path");
const prompt =require('inquirer').createPromptModule();
const fs = require("fs");

exports.command = 'recommendation <file>'
exports.desc = 'get an interoperability recommendation for the a slice specified in <file>'
exports.builder = {}

exports.handler = function (argv) {
    let filepath = path.resolve(argv.file);
    let s;
    try {
        s = require(filepath);
    }catch (e) {
        throw new Error(`could not find slice for file ${argv.file}, please try again`);
    }

    let jsonObj = {slice:s};
    let requestBody = { json: true, body: jsonObj };
    let requestUri = config.intop_service_uri + "/interoperability/recommendation";

    request.post(requestUri, requestBody , (err, res, body) => {
        if (err) {
            console.err("Error connecting to the interoperability service. Please check the http uri of the interoperability service " +
                "(" + config.intop_service_uri + "). Detailed error message:")
            return console.err(err);
        }

        if(body.logs.length>0) {
            printRecommendationResults(body).then(()=>{

            let questions = [
                {
                    name: 'overwriteSlice',
                    message: 'Should the slice be updated with the recommendation?',
                    type: "confirm",
                    default: false
                }
            ];

            prompt(questions).then((ans)=>{
                if(ans.overwriteSlice){
                    overwriteFile(filepath, JSON.stringify(body.slice, null, 2), function (err) {
                        if (err) {
                            return console.log("Error writing file: " + err);
                        }
                    })
                }
            });
            });

        }else{
            console.info("No interoperability issues detected")
        }
    });
};

function printRecommendationResults(body){
    return new Promise((resolve, reject) => {



    console.info("Interoperability recommendation results: \n");
    if(body.logs.length>0) {
        for (let i = 0; i < body.logs.length; i++) {
            console.info((i+1) + ": \tError: \n\t\t\t"+ body.logs[i].error.nodes);
            console.info("\t\tRecommendation: \n\t\t\t"+ body.logs[i].recommendation.description);
            console.info("\t\tResource: \n\t\t\t"+ JSON.stringify(body.logs[i].recommendation.resource));
            console.info("\t\tNew Path: \n\t\t\t"+ body.logs[i].recommendation.path);
            console.info("");
        }
    }

    let questions = [
        {
            name: 'showSlice',
            message: 'Print the resulting slice?',
            type: "confirm",
            default: false
        }
    ];

        prompt(questions).then((ans)=>{
            if(ans.showSlice) {
                console.info("Slice with recommendations:");
                console.info(JSON.stringify(body.slice, null, 2));
            }

            resolve();
        });
    });

}

function overwriteFile(filepath, content, callback){
    fs.truncate(filepath, 0, function() {
        fs.writeFile(filepath, content, callback);
    });
}