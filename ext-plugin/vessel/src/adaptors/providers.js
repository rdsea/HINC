const axios = require('axios');
const config = require('../../config');

/**
 * gets the available resources provider information
 */
function getProvider(settings){
    return axios.get(`${config.ENDPOINT}/vessel`).then((res) => {
        let sampleConfig = res.data.sampleConfiguration;

        let availableResources = [];

        availableResources.push({
            providerUuid: config.ADAPTOR_NAME,
            resourceType: 'IOT_RESOURCE',
            name: `vessel`,
            controlPoints: [],
            dataPoints: [],
            type: 'SOFTWARE_UNIT',
            location: null,
            parameters:{
                egressAccessPoints: [{
                    applicationProtocol: "MQTT",
                    host: "broker host",
                    port: "broker port",
                    accessPattern: "PUBSUB",
                    networkProtocol: "IP",
                    qos: 0,
                    topics: ["boatId"]
                }],
                ingressAccessPoints: [],
            
                "boat": "MAERSK ADRIATIC",
                "status": "Expected",
                "arrival": "6/5/2018",
                "departure": "6/6/2018",
                "destination": "VLC",
                "terminal": "TCV STEVEDORING COMPANY S.A."
            },
            metadata: {
                "boatId": "maerskadriatic",
            },
        });

        let provider = {
            name: config.ADAPTOR_NAME,
            uuid: config.ADAPTOR_NAME,
            availableResources: availableResources,
        };

        return provider;
    })
}

module.exports.getProvider = getProvider;
