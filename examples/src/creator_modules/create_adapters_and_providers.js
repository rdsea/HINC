const fs = require('fs');
const path = require("path");
const properties = require('properties');



module.exports = {
    create:createAllAdaptorsAndProviders
}

function createAllAdaptorsAndProviders(config, compositionConfig){
    compositionConfig.localConfigs.forEach((localConfig) => {
        createAdaptorsAndProviders(config, compositionConfig, localConfig);
    })
}

function createAdaptorsAndProviders(config, compositionConfig, localConfig){
    let adaptorConfigBase = require('../configTempates/adaptor.json');
    adaptorConfigBase.URI = config.broker_uri;
    adaptorConfigBase.EXCHANGE =  localConfig["adaptor.amqp.input"];

    let noderedAdaptorConfig = JSON.parse(JSON.stringify(adaptorConfigBase));
    noderedAdaptorConfig.ADAPTOR_NAME = `nodered_adapter_${localConfig["hinc.local.id"]}`;
    noderedAdaptorConfig.ENDPOINT = `http://noderedadapter${localConfig["hinc.local.id"]}:3004`;

    writeNODEREDAdaptorConfigJSON(noderedAdaptorConfig);

    let noderedProviderService = {
        image: "rdsea/nodered-provider"
    }


    let noderedAdaptorService = {
        image: "rdsea/nodered-adaptor",
        /*volumes: [
            `./config/${noderedAdaptorConfig.ADAPTOR_NAME}.js:/nodered/config.js`
        ],*/
        configs:[{source:"nodered_adapter_config",
            target: "/noderedadaptor/config/production.json"}]
    }

    /*
    configs:[{source: 'intop_config',
               target: '/rsihubintop/config/production.json'}]
    };
    */

    let service_config = {
        file: `./config/${noderedAdaptorConfig.ADAPTOR_NAME}.json`
    };

    compositionConfig.docker.configs[`nodered_adapter_config`] = service_config;

    compositionConfig.docker.services[`noderedprovider${localConfig["hinc.local.id"]}`] = noderedProviderService;
    // the amqp provider is in the cloud

    compositionConfig.docker.services[noderedAdaptorConfig.ADAPTOR_NAME] = noderedAdaptorService;

}

function writeAdaptorConfig(adaptorConfig){
    let config = `module.exports = {
        ADAPTOR_NAME : '${adaptorConfig.ADAPTOR_NAME}',
        URI : '${adaptorConfig.URI}',
        EXCHANGE : '${adaptorConfig.EXCHANGE}',
        ENDPOINT: '${adaptorConfig.ENDPOINT}'
    }`

    fs.writeFileSync(path.join(__dirname, `../../result/config/${adaptorConfig.ADAPTOR_NAME}.js`), config);
}

function writeNODEREDAdaptorConfigJSON(adaptorConfig){

    let config = `{
      "noderedadaptor": {
        "ADAPTOR_NAME" : "${adaptorConfig.ADAPTOR_NAME}",
            "URI" : "${adaptorConfig.URI}",
            "EXCHANGE" : "${adaptorConfig.EXCHANGE}",
            "ENDPOINT" : "${adaptorConfig.ENDPOINT}"
      }
    }`;

    fs.writeFileSync(path.join(__dirname, `../../result/config/${adaptorConfig.ADAPTOR_NAME}.json`), config);
}