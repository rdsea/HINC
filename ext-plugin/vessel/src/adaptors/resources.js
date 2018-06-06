const axios = require('axios');
const config = require('../../config');

/**
 * gets the available resources from the provider
 */

 function getItems(){
    console.log("fetching vessels")
    let resources = [];
     return axios.get(`${config.ENDPOINT}/vessel/list`).then((res) => {
         let vessels = res.data;
         vessels.forEach((vessel) => {
            resources.push({
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
                        host: vessel.broker.host,
                        port: vessel.broker.port,
                        accessPattern: "PUBSUB",
                        networkProtocol: "IP",
                        qos: 0,
                        topics: [vessel.boatId]
                    }],
                    ingressAccessPoints: [],
                
                    "boat": vessel.vessel.boat,
                    "status": vessel.vessel.status,
                    "arrival": vessel.vessel.arrival,
                    "departure": vessel.vessel.departure,
                    "destination": vessel.vessel.destination,
                    "terminal": vessel.vessel.terminal
                },
                metadata: {
                    "boatId": vessel.boatId,
                },
            })
         })
         return resources;
     })
 }


module.exports.getItems = getItems;