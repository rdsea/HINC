const prompt = require('inquirer').createPromptModule();
const fs = require('fs');
const path = require('path');
const db = require('../../data/db');

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
                
            },
        }


        return db.providerDao().findOne({uuid: ans.resourceProviderUuid}).then((provider) => {
            if(!(provider))throw new Error(`provider ${ans.resourceProviderUuid} does not exist`)
            let managementPoint = null
            provider.managementPoints.forEach((point) => {
                if(point.uuid === ans.controlPointUuid){
                    managementPoint = point;
                }
            });
            if(managementPoint === null) throw new Error(`management point ${ans.controlPointUuid} does not exist`)

            template.parameters = managementPoint.parameters;
            template.controlType = managementPoint.controlType;
        }).then(() => {
            console.info('\nTEMPLATE ==========================================\n')
            console.log(JSON.stringify(template, null, 2));

            if(argv.file){
                let filepath = path.join(process.cwd(), argv.file);
                fs.writeFileSync(filepath, JSON.stringify(template, null, 2)); 
                console.log(`control template saved in ${filepath}`);
            }
        }).catch((err) => {
            console.err(err);
        })
        
    })

}