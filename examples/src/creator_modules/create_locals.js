const fs = require('fs');
const path = require("path");
const properties = require('properties');


module.exports = {
    create:createAllLocals
};

function createAllLocals(config, compositionConfig){
    for(let i=1;i<=config.local_count;i++){
        createLocal(`local${i}`, config, compositionConfig);
    }
}

function createLocal(localId, config, compositionConfig){
    let localConfig = require("../configTempates/local.json");
    localConfig["spring.rabbitmq.addresses"] = config.broker_uri;
    localConfig["hinc.local.id"] = localId;
    localConfig["adaptor.amqp.input"] = `adaptor_${localId}_input`;
    localConfig["adaptor.amqp.output.broadcast"] = `adaptor_${localId}_broadcast`;
    localConfig["adaptor.amqp.output.unicast"] = `adaptor_${localId}_unicast`;
    fs.writeFileSync(path.join(__dirname, `../../result/config/local.${localId}.properties`), properties.stringify(localConfig));

    let service = {
        image: "rdsea/rsihublocal",
        //TODO change to config
        volumes: [
            `./config/local.${localId}.properties:/application.properties`
        ],
        depends_on:[
            "global"
        ]
    };

    compositionConfig.docker.services[`local${localId}`] = service;

    compositionConfig.exchanges[localConfig["adaptor.amqp.input"]] = {
        type: 'fanout',
        queues:[
            localConfig["adaptor.amqp.input"]
        ],
    }

    compositionConfig.localConfigs.push(localConfig);
}
