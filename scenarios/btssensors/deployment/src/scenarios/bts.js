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
    let localConfig = require("../configTempates/local.json");
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

// adaptor and provider configs
for(let i=0;i<locals.length;i++){
    let adaptorConfigBase = require('../configTempates/adaptor.json');
    adaptorConfigBase.URI = config.broker_uri;
    adaptorConfigBase.EXCHANGE =  locals[i].config["adaptor.amqp.input"];

    let sensorAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let mqttAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let bigqueryAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let ingestionAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let amqpAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let firewallAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    let noderedAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));

    for(let j=0;j<config.provider_sets;j++){
        sensorAdaptorConfig.ADAPTOR_NAME = `sensor${locals[i].localId}set${j}`;
        mqttAdaptorConfig.ADAPTOR_NAME = `mqtt${locals[i].localId}set${j}`;
        bigqueryAdaptorConfig.ADAPTOR_NAME = `bigquery${locals[i].localId}set${j}`;
        ingestionAdaptorConfig.ADAPTOR_NAME = `ingestion${locals[i].localId}set${j}`;
        amqpAdaptorConfig.ADAPTOR_NAME = `amqp${locals[i].localId}set${j}`;
        firewallAdaptorConfig.ADAPTOR_NAME = `firewall${locals[i].localId}set${j}`
        noderedAdaptorConfig.ADAPTOR_NAME = `nodered${locals[i].localId}set${j}`

        sensorAdaptorConfig.ENDPOINT = `http://sensorprovider${locals[i].localId}set${j}`;
        mqttAdaptorConfig.ENDPOINT = `http://mqttprovider${locals[i].localId}set${j}`
        bigqueryAdaptorConfig.ENDPOINT = `http://bigqueryprovider${locals[i].localId}set${j}`
        ingestionAdaptorConfig.ENDPOINT = `http://ingestionprovider${locals[i].localId}set${j}`
        amqpAdaptorConfig.ENDPOINT = `https://customer.cloudamqp.com/api/instances`;
        firewallAdaptorConfig.ENDPOINT = `http://firewallprovider${locals[i].localId}set${j}`
        noderedAdaptorConfig.ENDPOINT = `http://noderedprovider${locals[i].localId}set${j}`

        let createAdaptorDeploy = function(adaptorName, mountPath, image){
            let template = require("../configTempates/adaptor/deployTemplate.json");
            let adaptorDeploy = JSON.parse(JSON.stringify(template));
            adaptorDeploy.metadata.name = adaptorName;
            adaptorDeploy.spec.template.metadata.labels.app = adaptorName;
            adaptorDeploy.spec.template.spec.containers[0].name = adaptorName;
            adaptorDeploy.spec.template.spec.containers[0].image = image;

            // mount config map to deployment
            adaptorDeploy.spec.template.spec.volumes.push({
                name: "config",
                configMap: { name: `config-${adaptorName}`}
            });

            // mount config map to container
            adaptorDeploy.spec.template.spec.containers[0].volumeMounts.push({
                name: "config",
                mountPath: `${mountPath}/config.js`,
                subPath: `config.js.${adaptorName}`
            });

            return adaptorDeploy;
        }

        // create adaptor deployment templates
        let sensorAdaptor = {
            config: sensorAdaptorConfig,
            deploy: createAdaptorDeploy(sensorAdaptorConfig.ADAPTOR_NAME, "/sensor", "rdsea/sensor-adaptor"),
        }

        let mqttAdaptor = {
            config: mqttAdaptorConfig,
            deploy: createAdaptorDeploy(mqttAdaptorConfig.ADAPTOR_NAME, "/mqtt", "rdsea/mqtt-adaptor"),
        }

        let bigqueryAdaptor = {
            config: bigqueryAdaptorConfig,
            deploy: createAdaptorDeploy(bigqueryAdaptorConfig.ADAPTOR_NAME, "/bigquery", "rdsea/bigquery-adaptor"),
        }

        let ingestionAdaptor = {
            config: ingestionAdaptorConfig,
            deploy: createAdaptorDeploy(ingestionAdaptorConfig.ADAPTOR_NAME, "/ingestion", "rdsea/ingestion-adaptor"),
        }

        let amqpAdaptor = {
            config: amqpAdaptorConfig,
            deploy: createAdaptorDeploy(amqpAdaptorConfig.ADAPTOR_NAME, "/adaptor", "rdsea/amqp-adaptor"),
        }

        let firewallAdaptor = {
            config: firewallAdaptorConfig,
            deploy: createAdaptorDeploy(firewallAdaptorConfig.ADAPTOR_NAME, "/adaptor", "rdsea/firewall-adaptor"),
        }

        let noderedAdaptor = {
            config: noderedAdaptorConfig,
            deploy: createAdaptorDeploy(noderedAdaptorConfig.ADAPTOR_NAME, "/adaptor", "rdsea/nodred-adaptor"),
        }

        locals[i].adaptors.push(
            sensorAdaptor,
            mqttAdaptor,
            bigqueryAdaptor,
            ingestionAdaptor,
            amqpAdaptor,
            firewallAdaptor,
            noderedAdaptor
        );

        // create deployment and service templates for providers

        let createProviderDeploy = function(providerName, port, image){
            let template = require("../configTempates/provider/deployTemplate.json");
            let deploy = JSON.parse(JSON.stringify(template));
            deploy.metadata.name = providerName;
            deploy.spec.template.metadata.labels.app = providerName;
            deploy.spec.template.spec.containers[0].name = providerName;
            deploy.spec.template.spec.containers[0].ports[0].containerPort = port;
            deploy.spec.template.spec.containers[0].image = image;
            return deploy;
        }

        let createProviderService = function(providerName, port){
            let template = require("../configTempates/provider/serviceTemplate.json");
            let service = JSON.parse(JSON.stringify(template));
            service.metadata.name = providerName;
            service.metadata.labels.app = providerName;
            service.spec.selector.app = providerName;
            service.spec.ports[0].targetPort = port;
            return service;
        }


        locals[i].providers.push(
            {
                providerName: `sensorprovider${locals[i].localId}set${j}`,
                deploy: createProviderDeploy(`sensorprovider${locals[i].localId}set${j}`, 3001, "rdsea/sensor-provider"),
                service: createProviderService(`sensorprovider${locals[i].localId}set${j}`, 3001)
            },
            {
                providerName: `mqttprovider${locals[i].localId}set${j}`,
                deploy: createProviderDeploy(`mqttprovider${locals[i].localId}set${j}`, 3002, "rdsea/mqtt-provider"),
                service: createProviderService(`mqttprovider${locals[i].localId}set${j}`, 3002)
            },
            {
                providerName: `bigqueryprovider${locals[i].localId}set${j}`,
                deploy: createProviderDeploy(`bigqueryprovider${locals[i].localId}set${j}`, 3000, "rdsea/bigquery-provider"),
                service: createProviderService(`bigqueryprovider${locals[i].localId}set${j}`, 3000)
            },
            {
                providerName: `ingestionprovider${locals[i].localId}set${j}`,
                deploy: createProviderDeploy(`ingestionprovider${locals[i].localId}set${j}`, 3003, "rdsea/ingestion-provider"),
                service: createProviderService(`ingestionprovider${locals[i].localId}set${j}`, 3003)
            },
            {
                providerName: `firewallprovider${locals[i].localId}set${j}`,
                deploy: createProviderDeploy(`firewallprovider${locals[i].localId}set${j}`, 3008, "rdsea/kube-firewall-provider"),
                service: createProviderService(`firewallprovider${locals[i].localId}set${j}`, 3008)
            },
            {
                providerName: `noderedprovider${locals[i].localId}set${j}`,
                deploy: createProviderDeploy(`noderedprovider${locals[i].localId}set${j}`, 3004, "rdsea/nodered-provider"),
                service: createProviderService(`noderedprovider${locals[i].localId}set${j}`, 3004)
            }
        );
    }
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
// setup broker
setupBroker(exchanges, config.broker_uri);

// create configmaps

// create global configmap
fs.writeFileSync("/tmp/application.properties.global", properties.stringify(global.config));
let out = execSync(`kubectl create configmap global --from-file=/tmp/application.properties.global`);
console.log(out.toString());

// create local, adaptor and provider configmaps
for(let i=0;i<locals.length;i++){
    // local configmap
    fs.writeFileSync(`/tmp/application.properties.${locals[i].localId}`, properties.stringify(locals[i].config));
    out = execSync(`kubectl create configmap config-${locals[i].localId} --from-file=/tmp/application.properties.${locals[i].localId}`);
    console.log(out.toString());

    let conevrtdaptorConfig = function (adaptorConfig){
        let config = `module.exports = {
            ADAPTOR_NAME : '${adaptorConfig.ADAPTOR_NAME}',
            URI : '${adaptorConfig.URI}',
            EXCHANGE : '${adaptorConfig.EXCHANGE}',
            ENDPOINT: '${adaptorConfig.ENDPOINT}'
        }`
    
        return config;
    };

    locals[i].adaptors.forEach((adaptor) => {
        fs.writeFileSync(`/tmp/config.js.${adaptor.config.ADAPTOR_NAME}`, conevrtdaptorConfig(adaptor.config));
        out = execSync(`kubectl create configmap config-${adaptor.config.ADAPTOR_NAME} --from-file=/tmp/config.js.${adaptor.config.ADAPTOR_NAME}`);
        console.log(out.toString());
    })
}

// create deployments and services

// create global deployment
fs.writeFileSync("/tmp/deploy-global.yml", JSON.stringify(global.deploy, null, 2));
out = execSync(`kubectl create -f /tmp/deploy-global.yml`);
console.log(out.toString());

// create global service
fs.writeFileSync("/tmp/service-global.yml", JSON.stringify(global.service, null, 2));
out = execSync(`kubectl create -f /tmp/service-global.yml`);
console.log(out.toString());

// deploy locals
locals.forEach((local) => {
    fs.writeFileSync(`/tmp/local-${local.localId}.yml`, JSON.stringify(local.deploy, null, 2));
    out = execSync(`kubectl create -f /tmp/local-${local.localId}.yml`);
    console.log(out.toString());
})

// deploy providers 
locals.forEach((local) => {
    // create provider deployment
    local.providers.forEach((provider) => {
        fs.writeFileSync(`/tmp/deploy-${provider.providerName}.yml`, JSON.stringify(provider.deploy, null, 2));
    out = execSync(`kubectl create -f /tmp/deploy-${provider.providerName}.yml`);
    console.log(out.toString());

    // create provider service
    fs.writeFileSync(`/tmp/service-${provider.providerName}.yml`, JSON.stringify(provider.service, null, 2));
    out = execSync(`kubectl create -f /tmp/service-${provider.providerName}.yml`);
    console.log(out.toString());
    })
})

// deploy adaptors
locals.forEach((local) => {
    // create adaptor deployment
    local.adaptors.forEach((adaptor) => {
        fs.writeFileSync(`/tmp/deploy-${adaptor.config.ADAPTOR_NAME}.yml`, JSON.stringify(adaptor.deploy, null, 2));
        let out = execSync(`kubectl create -f /tmp/deploy-${adaptor.config.ADAPTOR_NAME}.yml`);
        console.log(out.toString());
    });

})