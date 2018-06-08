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

const execSync = child_process.execSync;
const writeFileSync = fs.writeFileSync;

function createProxy(sliceId, resourceId){
    let deployId = `linkerd-${sliceId}-${resourceId}`;
    return db.meshDao().findOne({sliceId, type: 'NAMESERVER'}).then((nameserver) => {
        _createConfigMap(resourceId, deployId, nameserver.location);
        _provisionProxy(deployId);

        let location = _waitForIp(deployId);
        return db.meshDao().insert({
            id: deployId,
            type: 'PROXY',
            createdAt: moment().unix(),
            sliceId,
            resourceId,
            location: `http://${location}:7474`,
            ip: location,
            port: 7474
        })
    });
}

function getProxyInfo(sliceId, resourceId){
    return db.meshDao().findOne({
        type: 'PROXY',
        sliceId,
        resourceId,
    });
}

function _provisionProxy(deployId){
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

    writeFileSync(`/tmp/deploy-${deployId}.json`, JSON.stringify(deployTemplate), 'utf8')
    let out = execSync(`kubectl create -f /tmp/deploy-${deployId}.json`).toString();
    console.debug(out);
    _exposeProxy(deployId);
}

function _exposeProxy(deployId){
    let serviceTemplate = JSON.parse(JSON.stringify(linkerdServiceTemplate));;
    serviceTemplate.metadata.labels.app = deployId;
    serviceTemplate.metadata.name = deployId;
    serviceTemplate.spec.selector.app = deployId;

    writeFileSync(`/tmp/service-${deployId}.json`, JSON.stringify(serviceTemplate), 'utf8');
    let out = execSync(`kubectl create -f /tmp/service-${deployId}.json`).toString();
    console.debug(out);
    return deployId;    
}

function _createConfigMap(resourceId, deployId, nameserverUri){
    let config = JSON.parse(JSON.stringify(linkerdConfigTemplate));
    config.routers[0].servers[0].dstName = `/cluster/${resourceId}`;
    config.routers[0].interpreter.baseUrl = nameserverUri;

    let out = execSync(`kubectl create configmap ${deployId} --from-literal=dummy=dummy`);
    console.debug(out.toString());
    out = execSync(`kubectl get configmap ${deployId} -o json`).toString();
    console.debug(out)
    let configmap = JSON.parse(out);

    let data = configmap.data;
    data['linkerd-tcp.yml'] = JSON.stringify(config);
    out = execSync(`kubectl patch configmap ${deployId} -p '{"data": ${JSON.stringify(data)}}'`).toString();         
    console.debug(out);
}

function _waitForIp(deployId){
    console.debug('waiting for proxy ip to become available')
    let res = "";
    try{
        while(!ipRegex({exact: true}).test(res)){
            let out = execSync(`kubectl get svc ${deployId} --template="{{range .status.loadBalancer.ingress}}{{.ip}}{{end}}"`);    
            res = out.toString();  
        }
    }catch(err){
        console.debug(err.toString());
        throw new Error('failed to fetch ip from kubectl');
    }
    console.debug(`IP for ${deployId} is ${res}`);
    return res;
}

module.exports = {
    createProxy,
    getProxyInfo
}
