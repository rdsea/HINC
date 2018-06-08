const promisify = require('util').promisify;
const child_process = require('child_process');
const fs = require('fs');
const db = require('../../data/db');
const linkerdDeployTemplate = require('./deployTemplate/linkerd_deploy');
const linkerdServiceTemplate = require('./deployTemplate/linerd_service');
const linkerdConfigTemplate = require('./deployTemplate/linkerd_config');
const axios = require('axios');
const ipRegex = require('ip-regex');
const moment = require('moment');
const nameserverService = require('./nameserver');

const exec = promisify(child_process.exec);
const writeFile = promisify(fs.writeFile);

function createProxy(sliceId, resourceId, accessPointNb){
    let deployId = `linkerd-${sliceId}-${resourceId}`;
    return db.meshDao().findOne({sliceId, type: 'NAMESERVER'}).then((nameserver) => {
        return _createConfigMap(resourceId, deployId, nameserver.location, accessPointNb);
    }).then(() => {
        return _provisionProxy(deployId, accessPointNb);
    }).then(() => {
        let proxy = {
            id: deployId,
            type: 'PROXY',
            createdAt: moment().unix(),
            sliceId,
            resourceId,
            location: `http://${deployId}:7474`,
            ip: deployId,
        }

        let ports = [];
        for(let i=0;i<accessPointNb;i++){
            ports.push(7474+i);
        }

        proxy.ports = ports;
        return db.meshDao().insert(proxy);
    });
}

function getProxyInfo(sliceId, resourceId){
    return db.meshDao().findOne({
        type: 'PROXY',
        sliceId,
        resourceId,
    });
}

function _provisionProxy(deployId, accessPointNb){
    let deployTemplate = JSON.parse(JSON.stringify(linkerdDeployTemplate));
    deployTemplate.metadata.name = deployId;
    deployTemplate.spec.template.metadata.labels.app = deployId;

    deployTemplate.spec.template.spec.volumes.push({
        name: deployId,
        configMap: { name: deployId }
    });

    deployTemplate.spec.template.spec.containers[0].volumeMounts.push({
        name: deployId,
        mountPath: `/io.buoyant/linkerd-tcp/config/`,
    });
    
    return writeFile(`/tmp/deploy-${deployId}.json`, JSON.stringify(deployTemplate), 'utf8').then(() => {
        return exec(`kubectl create -f /tmp/deploy-${deployId}.json`)
    }).then((res) => {
        if(res.stderr) {
            console.error(res.stderr);
            throw new Error('error occurred creating linkerd proxy deployment');
        }
        console.debug(res.stdout);
        return _exposeProxy(deployId, accessPointNb);
    })
}

function _exposeProxy(deployId, accessPointNb){
    let serviceTemplate = JSON.parse(JSON.stringify(linkerdServiceTemplate));;
    serviceTemplate.metadata.labels.app = deployId;
    serviceTemplate.metadata.name = deployId;
    serviceTemplate.spec.selector.app = deployId;

    for(let i=0;i<accessPointNb;i++){
        serviceTemplate.spec.ports.push({
            port: 7474+i,
            targetPort: 7474+i,
            name: `accesspoint${i}`
        })
    }

    return writeFile(`/tmp/service-${deployId}.json`, JSON.stringify(serviceTemplate), 'utf8').then(() => {
        return exec(`kubectl create -f /tmp/service-${deployId}.json`);
    }).then((res) => {
        if(res.stderr) {
            console.error(res.stderr);
            throw new Error('error occurred creating linkerd proxy service');
        }
        console.debug(res.stdout);
        return deployId
    })
}

function _createConfigMap(resourceId, deployId, nameserverUri, accessPointNb){
    let config = JSON.parse(JSON.stringify(linkerdConfigTemplate));
    for(let i=0;i<accessPointNb;i++){
        config.routers[0].servers.push( {
            ip: "0.0.0.0",
            port: 7474+i,
            dstName: `/${resourceId}${i}`,
            connectTimeoutMs: 10000
        });
    }
    config.routers[0].interpreter.baseUrl = nameserverUri;

    return exec(`kubectl create configmap ${deployId} --from-literal=dummy=dummy`).then((res) => {
        if(res.stderr) {
            console.error(res.stderr);
            throw new Error('error occurred creating linkerd proxy config map');
        }
        console.debug(res.stdout);
        return exec(`kubectl get configmap ${deployId} -o json`);
    }).then((res) => {
        if(res.stderr) {
            console.error(res.stderr);
            throw new Error('error occurred creating linkerd proxy config map');
        }
        console.debug(res.stdout);
        let configmap = JSON.parse(res.stdout);

        let data = configmap.data;
        data['linkerd-tcp.yml'] = JSON.stringify(config);
        return exec(`kubectl patch configmap ${deployId} -p '{"data": ${JSON.stringify(data)}}'`)
    }).then((res) => {
        if(res.stderr) {
            console.error(res.stderr);
            throw new Error('error occurred creating linkerd proxy config map');
        }
        console.debug(res.stdout);
    });
}

module.exports = {
    createProxy,
    getProxyInfo
}

