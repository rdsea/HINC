const Configstore = require('configstore');
const pkg = require('../package.json');
const prompt = require('inquirer').createPromptModule(); 

const conf = new Configstore(pkg.name, {});

exports.uri = conf.get('globalUri');
exports.intop_service_uri = conf.get('intopUri');

exports.hasConfig = () => {
    let hasUri = conf.get('globalUri') !== undefined && conf.get('globalUri') !== null
    let hasIntopUri = conf.get('intopUri') !== undefined && conf.get('intopUri') !== null
    let hasConfig = hasUri && hasIntopUri;
    return hasConfig
}

exports.setConfig = () => {
    let questions = [
        {
            name: 'globalUri',
            message: 'global management service url',
            default: 'http://localhost:8080'
        },
        {
            name: 'intopUri',
            message: 'interoperability service url',
            default: 'http://localhost:8081'
        },
    ]

    prompt(questions).then((ans) => {
        conf.set('globalUri', ans.globalUri)
        conf.set('intopUri', ans.intopUri)
        
        console.log('configuration set :')
        console.log(JSON.stringify(ans, null, 2));
    })
}

exports.getConfig = () => {
    return conf.all
}

//exports.production = true;
