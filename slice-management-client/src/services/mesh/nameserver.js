const promisify = require('util').promisify;
const child_process = require('child_process');
const fs = require('fs');
const db = require('../../data/db');
const namerdDeployTemplate = require('./deployTemplate/namerd_deploy');
const namerdServiceTemplate = require('./deployTemplate/namerd_service');
const namerdConfigTemplate = require('./deployTemplate/namerd_config');
const axios = require('axios');
const ipRegex = require('ip-regex');
const moment = require('moment');

const exec = promisify(child_process.exec);
const execSync = child_process.execSync;
const writeFile = promisify(fs.writeFile);

function createNameServer(sliceId){
    let deployId = `namerd-${sliceId}`;
    return _createNamerdConfig(namerdConfigTemplate, deployId).then(() => {
        return _createNamerdNames(deployId);
    }).then(() => {
        return _provisionNameServer(deployId);        
    }).then(() => {
        let location = _waitForIp(deployId);
        let mesh = {
            id: deployId,
            type: 'NAMESERVER',
            createdAt: moment().unix(),
            sliceId,
            location: `${location}:4180`,
        };
        console.debug(`creating mesh`);
        console.debug(JSON.stringify(mesh, null, 2));
        db.meshDao().insert(mesh);
    })

}

function deleteNameserver(sliceId){
    db.meshDao().findOne({sliceId, type: 'NAMESERVER'}).then((nameserver) => {
        let out = execSync(`kubectl delete configmap disco-${nameserver.id}`).toString();
        console.debug(out);
        out = execSync(`kubectl delete service ${nameserver.id}`).toString();
        console.debug(out);
        out = execSync(`kubectl delete configmap ${nameserver.id}`).toString();
        console.debug(out);
        out = execSync(`kubectl delete deployment ${nameserver.id}`).toString();
        console.debug(out);        
    })
}

function setName(sliceId, name, host, port){
    db.meshDao().findOne({sliceId, type: 'NAMESERVER'}).then((nameserver) => {
        let out = execSync(`kubectl get configmap disco-${nameserver.id} -o json`).toString();
        let configmap = JSON.parse(out);

        let data = configmap.data;
        data[name] = `${host} ${port}`;
        out = execSync(`kubectl patch configmap disco-${nameserver.id} -p '{"data": ${JSON.stringify(data)}}'`);         
        console.debug(out);
    })
}

function _waitForIp(deployId){
    let res = "";
    try{
        while(!ipRegex({exact: true}).test(res)){
            let out = execSync(`kubectl get svc ${deployId} --template="{{range .status.loadBalancer.ingress}}{{.ip}}{{end}}"`);    
            res = out.toString();  
            console.debug(res)
        }
    }catch(err){
        console.debug(err.toString());
        throw new Error('failed to fetch ip from kubectl');
    }
    console.debug(`IP for ${deployId} is ${res}`);
    return res;
}

function _provisionNameServer(deployId){
    let deployTemplate = JSON.parse(JSON.stringify(namerdDeployTemplate));
    deployTemplate.metadata.name = deployId;
    deployTemplate.spec.template.metadata.labels.app = deployId;

    deployTemplate.spec.template.spec.volumes.push({
        name: deployId,
        configMap: { name: deployId }
    });

    deployTemplate.spec.template.spec.containers[0].volumeMounts.push({
        name: deployId,
        mountPath: `/io.buoyant/linkerd/config/${deployId}.yml`,
        subPath: `${deployId}.yml`,
    });

    deployTemplate.spec.template.spec.volumes.push({
        name: "disco",
        configMap: { name: `disco-${deployId}` },
    });

    deployTemplate.spec.template.spec.containers[0].volumeMounts.push({
        name: "disco",
        mountPath: `/disco`,
    });

    deployTemplate.spec.template.spec.containers[0].args.push(`/io.buoyant/linkerd/config/${deployId}.yml`)

    return writeFile(`/tmp/deploy-${deployId}.json`, JSON.stringify(deployTemplate), 'utf8').then(() => {
        return exec(`kubectl create -f /tmp/deploy-${deployId}.json`);
    }).then((res) => {
        if(res.stderr) {
            console.err(res.stderr);
            throw new Error('error occurred provisioning broker');
        }
        console.debug(res.stdout);
        return _exposeNameServer(deployId);
    }).then(() => {
        return deployId;
    })
}

function _exposeNameServer(deployId){
    let serviceTemplate = namerdServiceTemplate;
    serviceTemplate.metadata.labels.app = deployId;
    serviceTemplate.metadata.name = deployId;
    serviceTemplate.spec.selector.app = deployId;
    return writeFile(`/tmp/service-${deployId}.json`, JSON.stringify(serviceTemplate), 'utf8').then(() => {
        return exec(`kubectl create -f /tmp/service-${deployId}.json`);
    }).then((res) => {
        if(res.stderr) {
            console.err(res.stderr);
            throw new Error('error occurred exposing nameserver');
        }
        console.debug(res.stdout);
        return deployId;
    });
}

function _createNamerdConfig(config, deployId){
    return writeFile(`/tmp/${deployId}.yml`, JSON.stringify(config), 'utf8').then(() => {
        return exec(`kubectl create configmap ${deployId} --from-file=/tmp/${deployId}.yml`);
    }).then((res) => {
        if(res.stderr) throw new Error('error occurred creating ingestion client config');
        console.debug(res.stdout);
        return config;
    });
}

function _createNamerdNames(deployId){
    let out = execSync(`kubectl create configmap disco-${deployId}`);
    console.debug(out.toString());
}


module.exports = {
    createNameServer,
    setName,
    deleteNameserver
}