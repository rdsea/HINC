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


proxyService.createProxy("test", "testresource", 2);
//nameserverService.setName("test", "testresource0", "broker1528468419792", 1883);
//nameserverService.setName("test", "testresource1", "broker1528468426344", 1883);
