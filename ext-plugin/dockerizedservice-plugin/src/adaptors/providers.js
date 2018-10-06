const axios = require('axios');
const configModule = require('config');
const config = configModule.get('dockerizedservice');

function getProvider(){
    let provider = null;
    return axios.get(`${config.ENDPOINT}`).then((res) => {
        let serviceDescriptions = res.data;
        //console.log(serviceDescriptions);
        //adaptor uuid is mapped to uuid of provider as we
        //have one adaptor per provider
        let availableResources = [];
            availableResources.push({
                plugin: config.ADAPTOR_NAME,
                providerUuid: config.ADAPTOR_UUID,
                resourceType: 'IOT_RESOURCE',
                name: config.ADAPTOR_NAME,
                controlPoints: [
                  {
                    "api":`${config.ENDPOINT}`,
                    "method":"POST",
                    "parameters":serviceDescriptions.ampleConfiguration
                  }
                ],
                dataPoints: [],
                type: 'ENGINE',
                location: null,

                parameters:{
                    ingressAccessPoints:[],
                    egressAccessPoints: [{
                        applicationProtocol: "HTTP",
                        host: "target host",
                        port: "targetport",
                        accessPattern: "DIRECT",
                        networkProtocol: "IP"
                    }],
                },
                metadata: {
                },
            }    )

        let provider = {
            name: config.ADAPTOR_NAME,
            uuid: config.ADAPTOR_UUID,
            availableResources: availableResources,
        };
        return provider;
    });
}

module.exports.getProvider = getProvider;
