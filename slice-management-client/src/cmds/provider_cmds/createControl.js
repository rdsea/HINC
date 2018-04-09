const prompt = require('inquirer').createPromptModule();

exports.command = 'create-control'
exports.desc = 'creates a control template in JSON, complete this template to use for the control command'
exports.builder = {}

let questions = [
    {
        name: 'name',
        message: 'create a name for this new control: '
    },
    {
        name: 'resourceProviderUuid',
        message: 'resource provider uuid: ',
    },
    {
        name: 'controlPointUuid',
        message: 'control point uuid: '
    },
    {
        name: 'controlType',
        message: 'control type: ',
        type: 'list',
        choices: ['PROVISION', 'CONFIGURE', 'EXECUTE', 'REMOVE']
    }

]

exports.handler = function (argv) {
    prompt(questions).then((ans) => {
        let template = {
            controlPointUuid: ans.controlPointUuid,
            resourceProviderUuid: ans.resourceProviderUuid,
            name: ans.name,
            controlType: ans.controlType,
            parameters:{
                field: 'valid json object'
            },
        }

        console.info('\nTEMPLATE ==========================================\n')
        console.log(JSON.stringify(template, null, 2));
    })

}