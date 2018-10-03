const prompt = require('inquirer').createPromptModule();
const questions = require('../questions');
const properties = require('properties');
const fs = require('fs');
const yml = require('js-yaml');
const setupBroker = require('../amqpTools');
const path = require("path");

const createGlobal = require("../creator_modules/create_global");
const createLocals = require("../creator_modules/create_locals");
const createAdaptersAndProviders = require("../creator_modules/create_adapters_and_providers");
const createIntop = require("../creator_modules/create_intop");
const createSas = require("../creator_modules/create_sas");


let dockerComposeHinc = {
    version: '3.3',
    services:{},
    configs:{}
}

let exchanges = {};
let localConfigs = [];

let compositionConfig = {
    docker: dockerComposeHinc,
    exchanges:exchanges,
    localConfigs: localConfigs
}

//prompt(questions).then((ans) => {
{
    clearResult()

    let config = {
        local_count:1,
        provider_count:1,
        broker_uri:"amqp://mgrpueon:5A9m3ovJBL18QQXQQll-hAFnTW9xX5wV@sheep.rmq.cloudamqp.com/mgrpueon",
        database_uri:""
    };

    console.log('parameters set :')
    console.log(JSON.stringify(config, null, 2));
    create(config);

    setupBroker(exchanges, config.broker_uri);
    fs.writeFileSync(path.join(__dirname, "../../result/docker-compose.yml"), yml.safeDump(compositionConfig.docker));
    
}

function create(config){
    createGlobal.create(config, compositionConfig);
    createLocals.create(config, compositionConfig);
    createAdaptersAndProviders.create(config, compositionConfig);
    createIntop.create(config, compositionConfig);
    createSas.create(config, compositionConfig);

    console.log("navigate to the folder ./result and run the command:")
    console.log("$ docker-compose up");
}


function clearResult(){
    if(fs.existsSync('result')){
        var deleteFolderRecursive = function(path) {
            if (fs.existsSync(path)) {
                fs.readdirSync(path).forEach(function(file, index){
                var curPath = path + "/" + file;
                if (fs.lstatSync(curPath).isDirectory()) { // recurse
                    deleteFolderRecursive(curPath);
                } else { // delete file
                    fs.unlinkSync(curPath);
                }
                });
                fs.rmdirSync(path);
            }
        };

        deleteFolderRecursive('result');
    }

    fs.mkdirSync('result');
    fs.mkdirSync('result/config');
}
