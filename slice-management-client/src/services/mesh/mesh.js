const nameserverService = require('./nameserver');
const proxyService = require('./proxy');


function deleteMesh(sliceId){
    nameserverService.deleteNameserver(sliceId).then(() => {
        return proxyService.deleteAllProxies(sliceId);     
    });
}

module.exports = {
    createNameServer: nameserverService.createNameServer,
    setName: nameserverService.setName,
    deleteNameserver: nameserverService.deleteNameserver,
    createProxy: proxyService.createProxy,
    getProxyInfo: proxyService.getProxyInfo,
    setNames: nameserverService.setNames,
    deleteMesh: deleteMesh
}


