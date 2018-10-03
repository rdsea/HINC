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
    let localConfig = JSON.parse(JSON.stringify(require("../configTemplates/local.json")));
    localConfig["spring.rabbitmq.addresses"] = config.broker_uri;
    localConfig["hinc.local.id"] = localId;
    localConfig["adaptor.amqp.input"] = `adaptor_${localId}_input`;
    localConfig["adaptor.amqp.output.broadcast"] = `adaptor_${localId}_broadcast`;
    localConfig["adaptor.amqp.output.unicast"] = `adaptor_${localId}_unicast`;
    fs.writeFileSync(path.join(__dirname, `../../result/config/local.${localId}.properties`), properties.stringify(localConfig));

    let service = {
        image: "rdsea/rsihublocal",
        depends_on:[
            "rsihubglobal"
        ]
    };

    addServiceConfigDocker(config, compositionConfig, service, localId);


    compositionConfig.docker.services[`local${localId}`] = service;

    compositionConfig.exchanges[localConfig["adaptor.amqp.input"]] = {
        type: 'fanout',
        queues:[
            localConfig["adaptor.amqp.input"]
        ],
    }

    compositionConfig.localConfigs.push(localConfig);
}




function addServiceConfigDocker(config, compositionConfig, service, localId){
    let config_name = `local.${localId}_config`;
    let config_filepath = `./config/local.${localId}.properties`;
    let config_target = '/rsihublocal/application.properties';

    if(config.is_docker_stack){
        service.configs = [{source: config_name,
            target: config_target}];

        compositionConfig.docker.configs[config_name] = {
            file: config_filepath
        };
    }else{
        service.volumes = [
            `${config_filepath}:${config_target}`
        ]
    }
}
