const axios = require('axios');
const configModule = require('config');
const config = configModule.get('dockerizedservice');
const parse = require("url-parse");

function getItems(){
    console.log("Obtaining resources from the provider ",config.ENDPOINT);
    return axios.get(`${config.ENDPOINT}/list`).then((res) => {
        let items = res.data;
        let getDockerizedServicePromises = [];
        items.forEach((item) => {
            getDockerizedServicePromises.push(axios.get(`${config.ENDPOINT}/${item.serviceId}`).then(res => res.data));
        })
        return Promise.all(getDockerizedServicePromises);
    }).then((dockerizedservices) => {
        let resources = [];

        dockerizedservices.forEach((dockerizedservice) => {
          console.log(dockerizedservice);
            let url = parse(config.ENDPOINT);
            let ingressAccessPoint = {
                applicationProtocol: "HTTP",
                host: url.hostname,
                port: dockerizedservice.ports,
                accessPattern: "DIRECT",
                networkProtocol: "IP",
            };

            resources.push({
                uuid: dockerizedservice.serviceId,
                providerUuid: config.ADAPTOR_UUID,
                resourceType: 'IOT_RESOURCE',
                name: dockerizedservice.image,
                controlPoints: [
                ],
                dataPoints: [],
                type: dockerizedservice.image,
                location: null,
                parameters:{
                    ingressAccessPoints:[ingressAccessPoint],
                    egressAccessPoints: [],
                },
                metadata: {
                },
            })
        });
        return resources;
    })
}
getItems().then((res) => console.log(res));
module.exports.getItems = getItems;
