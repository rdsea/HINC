const fs = require('fs');
const path = require("path");
const properties = require('properties');


module.exports = {create: createGlobal};

function createGlobal(config, compositionConfig){
    let globalConfig = require("../configTempates/global.json");
    globalConfig['spring.rabbitmq.addresses'] = config.broker_uri

    fs.writeFileSync(path.join(__dirname, "../../result/config/global.properties"), properties.stringify(globalConfig));

    let service = {
        image: "rdsea/rsihubglobal",
        ports:[
            "8080:8080"
        ],
        volumes: [
            "./config/global.properties:/application.properties"
        ],
        depends_on:[]
    };

    compositionConfig.docker.services['global'] = service
    compositionConfig.exchanges[globalConfig["hinc.global.rabbitmq.input"]] = {
        type: 'fanout',
        queues:[
            globalConfig["hinc.global.rabbitmq.input"]
        ],
    }
}