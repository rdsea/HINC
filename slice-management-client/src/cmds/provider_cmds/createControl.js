const prompt = require('inquirer').createPromptModule();
const fs = require('fs');
const path = require('path');

exports.command = 'create-control [options]'
exports.desc = 'creates a control template in JSON, complete this template to use for the control command'
exports.builder = {
    file:{
        alias: '-f',
        describe: 'output the finished template into a file',
        type: 'string'
    },
}

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
        message: 'provider management point uuid: '
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

        if(argv.file){
            let filepath = path.join(process.cwd(), argv.file);
            fs.writeFileSync(filepath, JSON.stringify(template, null, 2)); 
            cosole.log(`control template saved in ${filepath}`);
        }
    })

}