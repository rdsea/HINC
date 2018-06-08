const nameserverService = require('./nameserver');
const proxyService = require('./proxy');

module.exports = {
    createNameServer: nameserverService.createNameServer,
    setName: nameserverService.setName,
    deleteNameserver: nameserverService.deleteNameserver,
    createProxy: proxyService.createProxy,
    flush: nameserverService.flush,
    getProxyInfo: proxyService.getProxyInfo,
}

