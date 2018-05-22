const Configstore = require('configstore');
const pkg = require('../package.json');
const prompt = require('inquirer').createPromptModule(); 

const conf = new Configstore(pkg.name, {});

exports.uri = conf.get('uri');
exports.exchange = conf.get('exchange');
exports.routingKey = conf.get('routingKey');
exports.localRoutingKey = conf.get('localRoutingKey');
exports.globalManagementHttp = conf.get('globalManagementHttp');

exports.hasConfig = () => {
    let hasUri = conf.get('uri') !== undefined && conf.get('uri') !== null
    let hasExchange = conf.get('exchange') !== undefined && conf.get('exchange') !== null
    let hasRoutingKey = conf.get('routingKey') !== undefined && conf.get('routingKey') !== null
    let hasLocalRoutingKey = conf.get('localRoutingKey') !== undefined && conf.get('localRoutingKey') !== null
    let hasGlobalManagementHttp = conf.get('globalManagementHttp') !== undefined && conf.get('globalManagementHttp') !== null

    let hasConfig = hasUri && hasExchange && hasRoutingKey && hasLocalRoutingKey && hasGlobalManagementHttp;
    return hasConfig
}

exports.setConfig = () => {
    let questions = [
        {
            name: 'uri',
            message: 'amqp connection string',
            default: 'amqp://guest:guest@localhost'
        },
        {
            name: 'exchange',
            message: 'exchange name',
            default: 'test.adaptors'
        },
        {
            name: 'routingKey',
            message: 'amqp routing key used by pizza ',
            default: 'pizza'
        },
        {
            name: 'localRoutingKey',
            message: 'routing key used by the local management service',
            default: 'test.local'
        },
        {
            name: 'globalManagementHttp',
            message: 'http uri of the global management service (e.g: http://localhost:8080)'
        }
    ]

    prompt(questions).then((ans) => {
        conf.set('uri', ans.uri)
        conf.set('exchange', ans.exchange)
        conf.set('routingKey', ans.routingKey)
        conf.set('localRoutingKey', ans.localRoutingKey)
        conf.set('globalManagementHttp', ans.globalManagementHttp)
        
        console.log('configuration set :')
        console.log(JSON.stringify(ans, null, 2));
    })
}

exports.getConfig = () => {
    return conf.all
}

//exports.production = true;
