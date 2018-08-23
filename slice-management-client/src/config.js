const Configstore = require('configstore');
const pkg = require('../package.json');
const prompt = require('inquirer').createPromptModule(); 

const conf = new Configstore(pkg.name, {});

exports.uri = conf.get('globalUri');
exports.intop_service_uri = conf.get('intopUri');
exports.software_artefact_service_uri = conf.get('softwareArtefactUri');

exports.hasConfig = () => {
    let hasUri = conf.get('globalUri') !== undefined && conf.get('globalUri') !== null
    let hasIntopUri = conf.get('intopUri') !== undefined && conf.get('intopUri') !== null
    let hasSoftwareArtefactUri = conf.get('softwareArtefactUri') !== undefined && conf.get('softwareArtefactUri') !== null
    let hasConfig = hasUri && hasIntopUri && hasSoftwareArtefactUri;
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
        {
            name: 'softwareArtefactUri',
            message: 'software artefact service url',
            default: 'http://localhost:8082'
        }
    ]

    prompt(questions).then((ans) => {
        conf.set('globalUri', ans.globalUri)
        conf.set('intopUri', ans.intopUri)
        conf.set('softwareArtefactUri', ans.softwareArtefactUri)
        
        console.log('configuration set :')
        console.log(JSON.stringify(ans, null, 2));
    })
}

exports.getConfig = () => {
    return conf.all
}

//exports.production = true;
