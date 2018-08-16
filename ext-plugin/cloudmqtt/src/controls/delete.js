const axios = require('axios');
const config = require('../../config')

function deleteResource(resource){
    console.log('making call to delete resource '+resource.uuid);

    return _getAvailableBrokers().then((brokers) => {
        if(brokers.length <= 5) return

        return axios.delete(`${config.ENDPOINT}/${resource.uuid}`, {auth: { username: process.env.MQTT_KEY}})
    }).then((res) => {
        controlResult = {
            status: 'SUCCESS',
            rawOutput: JSON.stringify(resource),
            resourceUuid: resource.uuid,
        };
        console.log('successfuly deleted resource '+resource.uuid);
        console.log(JSON.stringify(controlResult, null, '\t'));
        return controlResult;
    }).catch((err) => {
        console.log('control execution failed');
        console.error(err.toString());
        controlResult = {
            status: 'FAILED',
            rawOutput: "",
        };

        console.log(controlResult);
        return controlResult;
    });
}

function _getAvailableBrokers(){
    return axios.get(`${config.ENDPOINT}`, {auth: { username: process.env.MQTT_KEY}}).then((res) => {
        let items = res.data;
        let getBrokerPromises = [];
        items.forEach((item) => {
            getBrokerPromises.push(axios.get(`${config.ENDPOINT}/${item.id}`, {auth: { username: process.env.MQTT_KEY}}).then(res => res.data));
        })
        return Promise.all(getBrokerPromises);
    })
}

module.exports.deleteResource = deleteResource;
