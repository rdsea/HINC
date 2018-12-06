const request = require('request');
const config = require("../../config");
const path = require("path");
const prompt =require('inquirer').createPromptModule();
const fs = require("fs");

const performdg = require("../../util/performance_data_generator");
const artefact_service = require("../../services/crud_artefact_service");
const intop_service = require("../../services/intop_service");

let createdArtefacts = [];
let resultsarray = [];

exports.command = 'performancetest <nodecount> <metadatacount> <iterations>'
exports.desc = ''
exports.builder = {}

exports.handler = function (argv) {
    let nodecount = 100;
    let metadatacount = 100;
    let iteration = 1;


    createArtefactMocks(metadatacount).then(()=>{
        return perform_tests(nodecount,metadatacount,iteration)
    }).then(()=>{
        saveResultsToFiles();
    }).then(()=>{
        cleanUpArtefactMocks();
    })
};


function saveResultsToFiles(){
    let promises = [];

    return Promise.all(promises);
}


function overwriteFile(filepath, content, callback){
    fs.truncate(filepath, 0, function() {
        fs.writeFile(filepath, content, callback);
    });
}

function createArtefactMocks(metadatacount){

    let artefactDirect = performdg.solution_artefact_direct(metadatacount);
    let artefactIndirect = performdg.solution_artefact_indirect(metadatacount);

    let promises= [];
    promises.push(artefact_service.createArtefact(artefactDirect).then((new_a)=>{createdArtefacts.push(new_a)}));
    promises.push(artefact_service.createArtefact(artefactIndirect).then((new_a)=>{createdArtefacts.push(new_a)}));
    return Promise.all(promises);
}

function cleanUpArtefactMocks(){
    createdArtefacts.forEach((artefact)=>{artefact_service.deleteArtefact(artefact)});
}



function saveResult(operation, instancename, nodecount, metadatacount, body){
    if(body.time) {
        const NS_PER_SEC = 1e9;
        const MS_PER_NS = 1e-6
        let diff = body.time;

        /*console.log("Performance: ");
        console.log(`Benchmark took ${diff[0] * NS_PER_SEC + diff[1]} nanoseconds`);
        console.log(`Benchmark took ${(diff[0] * NS_PER_SEC + diff[1]) * MS_PER_NS} milliseconds`);*/
        let ms = ((diff[0] * NS_PER_SEC + diff[1]) * MS_PER_NS);
        resultsarray.push({
            operation: operation,
            instancename: instancename,
            nodecount: nodecount,
            metadatacount: metadatacount,
            time_ms:ms
        });
    }else{
        console.log(`no body.time for:
                        operation ${operation}
                        instancename ${instancename}
                        nodecount ${nodecount}
                        metadatacount  ${metadatacount}`);
    }
}

function perform_tests(nodecount, metadatacount, iterations){
    return loopIterations(0,iterations-1,metadatacount,nodecount);
}

function loopIterations(i, iterations, metadatacount, nodecount){
    if(i<=iterations){
        return loopMetadata(i,1,metadatacount,nodecount).then(()=>{
            return loopIterations(i+1, iterations,metadatacount,nodecount);
        });
    }
}


function loopMetadata(i, m, metadatacount, nodecount){
    if(m<=metadatacount){
        return loopNodes(i,m,1,nodecount).then(()=>{
            return loopMetadata(i,m*10,metadatacount,nodecount);
        })
    }
}

function loopNodes(i, m, n, nodecount){
    if(n<=nodecount){
        return innerloop(n,m,i).then(()=>{
            return loopNodes(i,m,n*10,nodecount);
        });
    }
}

function innerloop(nodecount, metadatacount, iterations){
    console.log(`i: ${iterations}  m:${metadatacount}  n:${nodecount}`);

    let slice1 = performdg.direct_instance("direct_instance",nodecount, metadatacount, performdg.metadata_parameters_OK());
    let slice3 = performdg.indirect_instance("indirect_instance",nodecount, metadatacount, performdg.metadata_parameters_OK());


    let slice2 = performdg.direct_instance("direct_instance",nodecount, metadatacount, performdg.metadata_parameters_ERROR());
    let slice4 = performdg.indirect_instance("indirect_instance",nodecount, metadatacount, performdg.metadata_parameters_ERROR());

    return test(slice1, slice1.sliceId, nodecount, metadatacount)
        .then(()=>{
            return test(slice3, slice3.sliceId, nodecount, metadatacount);
        })
        .then(()=>{
            return test(slice2, slice2.sliceId, nodecount, metadatacount);
        })
        .then(()=>{
            return test(slice4, slice4.sliceId, nodecount, metadatacount);
        })
}


function test(slice, instancename, nodecount, metadatacount){
    return intop_service.check(slice, 300000).then((body)=>{
        return saveResult("check", instancename, nodecount, metadatacount, body);
    }).then(()=>{
        return intop_service.recommendation(slice, 300000);
    }).then((body)=>{
        return saveResult("recommendation", instancename, nodecount, metadatacount, body);
    });

}