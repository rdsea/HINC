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
    services:{}
}

let exchanges = {};
let localConfigs = [];

let compositionConfig = {
    docker: dockerComposeHinc,
    exchanges:exchanges,
    localConfigs: localConfigs
}

//{
prompt(questions).then((config) => {
    clearResult()

    /*let config = {
        local_count: 2,
        provider_count: 2,
        broker_uri: "amqp://mgrpueon:5A9m3ovJBL18QQXQQll-hAFnTW9xX5wV@sheep.rmq.cloudamqp.com/mgrpueon",
        database_uri: "",
        global_mongo_db: "mongodb://mongo:27017/global",
        local_mongo_db: "mongodb://mongo:27017/local",
        sas_mongo_db: "mongodb://testing:testing1@ds161102.mlab.com:61102/testing_database",
        intop_mongo_db: "mongodb://testing:testing1@ds161102.mlab.com:61102/testing_database",
        is_docker_stack: false
    };*/

    if (config.is_docker_stack) {
        compositionConfig.docker.configs = {};
    }

    console.log('parameters set :')
    console.log(JSON.stringify(config, null, 2));
    dockerizedMongo(config,compositionConfig);
    create(config);

    setupBroker(exchanges, config.broker_uri);

    if (config.is_docker_stack) {
        fs.writeFileSync(path.join(__dirname, "../../result/docker-stack.yml"), yml.safeDump(compositionConfig.docker));
    } else {
        fs.writeFileSync(path.join(__dirname, "../../result/docker-compose.yml"), yml.safeDump(compositionConfig.docker));
    }
//}
});

function create(config){
    createGlobal.create(config, compositionConfig);
    createLocals.create(config, compositionConfig);
    createAdaptersAndProviders.create(config, compositionConfig);
    createIntop.create(config, compositionConfig);
    createSas.create(config, compositionConfig);

    if(config.is_docker_stack){
        console.log("to deploy the stack to a docker swarm:")
        console.log("   1. navigate to the folder ./result")
        console.log("   2. Connect your shell to your swarm manager:");
        console.log("       $ eval $(docker-machine env <swarm-manager-name>");
        console.log("   3. run: ");
        console.log("       $ docker stack deploy -c docker-stack.yml <stack-name>");
    }else{
        console.log("navigate to the folder ./result and run the command:")
        console.log("$ docker-compose up");
    }
}

function dockerizedMongo(config, compositionConfig){
    if(config.local_mongo_db === "" || config.global_mongo_db === ""
        || config.sas_mongo_db === "" || config.intop_mongo_db ===""){
        compositionConfig.docker.services.mongo = {
            image: "mongo",
            ports: ["27017:27017"]
        }
    }
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
