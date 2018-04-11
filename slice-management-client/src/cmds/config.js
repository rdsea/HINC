const config = require('../config');

exports.command = 'config [options]'
exports.desc = 'manage pizza configuration'
exports.builder = {
    get:{
        alias: 'g',
        describe: 'get current pizza configuration',
        type: 'boolean',
        demandOption: false
    },
    set:{
        alias: 's',
        describe: 'reset pizza configuration',
        type: 'boolean',
        demandOption: false
    }
}

exports.handler = function (argv) {
    if(argv.get){
        console.log(JSON.stringify(config.getConfig(), null, 2));
    }else if(argv.set){
        return config.setConfig()
    }
}