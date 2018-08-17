const fs = require('fs');
const yml = require('js-yaml');
const setupBroker = require('../amqpTools');
const promisify = require("util").promisify;
const exec = promisify(require("child_process").exec);
const execSync = require("child_process").execSync;
const properties = require('properties');
const path = require("path");

// load config
let config = yml.safeLoad(fs.readFileSync(path.join(__dirname, "../../config.yml")));

// create global config
let global = {
    config: require("../configTempates/global.json"),
    globalId: "globalId",
}

global.config['spring.rabbitmq.addresses'] = config.broker_uri;
global.config["spring.data.mongodb.uri"] = config.mongodb_uri;

// create the correct kubernetes deployment template
let globalDeploy = require("../configTempates/global/deployTemplate.json");

// mount config map to deployment
globalDeploy.spec.template.spec.volumes.push({
    name: "config",
    configMap: { name: `global`}
});

// mount config map to container
globalDeploy.spec.template.spec.containers[0].volumeMounts.push({
    name: "config",
    mountPath: "/application.properties",
    subPath: `application.properties.global`
});

// create correct kubernetes service template
let globalService = require("../configTempates/global/serviceTemplate.json");

global.deploy = globalDeploy;
global.service = globalService;


let locals = [];
// create the configs for each local
for(let i=1;i<=config.local_count;i++){
    let localTemplate = require("../configTempates/local/deployTemplate.json")
    let localConfig = JSON.parse(JSON.stringify(localTemplate));
    let localId = `local${i}`;

    localConfig["spring.data.mongodb.uri"] = config.mongodb_uri;
    localConfig["spring.rabbitmq.addresses"] = config.broker_uri;
    localConfig["hinc.local.id"] = localId;
    localConfig["adaptor.amqp.input"] = `adaptor_${localId}_input`;
    localConfig["adaptor.amqp.output.broadcast"] = `adaptor_${localId}_broadcast`;
    localConfig["adaptor.amqp.output.unicast"] = `adaptor_${localId}_unicast`

    // create the correct kubernetes deployment template
    let template = require("../configTempates/local/deployTemplate.json")
    let deploy = JSON.parse(JSON.stringify(template));
    deploy.metadata.name = localId;
    deploy.spec.template.metadata.labels.app = localId;
    deploy.spec.template.spec.containers[0].name = localId;

    // mount config map to deployment
    deploy.spec.template.spec.volumes.push({
        name: "config",
        configMap: { name: `config-${localId}`}
    });

    // mount config map to container
    deploy.spec.template.spec.containers[0].volumeMounts.push({
        name: "config",
        mountPath: "/application.properties",
        subPath: `application.properties.${localId}`
    });

    locals.push({
        localId,
        config: localConfig,
        adaptors:[],
        providers:[],
        deploy,
    })
}

// setup the amqp input exchanges for local and global 
// we only need one global
exchanges = {
    hinc_global_input: {
        type: "fanout",
        queues: ["hinc_global_input"]
    }
}

for(let i=0;i<locals.length;i++){
    exchanges[locals[i].config["adaptor.amqp.input"]] = {
        type: 'fanout',
        queues:[
            locals[i].config["adaptor.amqp.input"]
        ],
    }
}

console.log(locals)
// setup broker
setupBroker(exchanges, config.broker_uri);

