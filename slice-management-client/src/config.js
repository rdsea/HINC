const Configstore = require('configstore');
const pkg = require('../package.json');
const prompt = require('inquirer').createPromptModule(); 

const conf = new Configstore(pkg.name, {});

exports.uri = conf.get('globalUri');

exports.hasConfig = () => {
    let hasUri = conf.get('globalUri') !== undefined && conf.get('globalUri') !== null
    let hasConfig = hasUri;
    return hasConfig
}

exports.setConfig = () => {
    let questions = [
        {
            name: 'globalUri',
            message: 'global management service url',
            default: 'http://localhost:8080'
        },
    ]

    prompt(questions).then((ans) => {
        conf.set('globalUri', ans.globalUri)
        
        console.log('configuration set :')
        console.log(JSON.stringify(ans, null, 2));
    })
}

exports.getConfig = () => {
    return conf.all
}

exports.production = true;
