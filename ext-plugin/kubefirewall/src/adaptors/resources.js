const axios = require('axios');
const config = require('../../config');

function getItems(){
    console.log("fetching resources");
    return axios.get(`${config.ENDPOINT}/kubefw/list`).then((res) => {
        let firewalls = res.data;
        let resources = [];
        firewalls.forEach(firewall => {
            resources.push({
                uuid: firewall.firewallId,
                providerUuid: config.ADAPTOR_NAME,
                resourceType: 'NETWORK_FUNCTION_SERVICE',
                name: `firewall`,
                controlPoints: [],
                dataPoints: [],
                type: 'FIREWALL',
                location: null,
                parameters:{
                    egressAccessPoints: [],
                    ingressAccessPoints: [],

                    serviceName: firewall.firewallId,
                    ingress: firewall.ingress,
                    egress: firewall.egress,
                },
                metadata: {
                },
            })
        });
        return resources;
    })
}

module.exports.getItems = getItems;
