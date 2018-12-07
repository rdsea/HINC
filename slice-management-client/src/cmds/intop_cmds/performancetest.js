const request = require('request');
const config = require("../../config");
const path = require("path");
const prompt =require('inquirer').createPromptModule();
const fs = require("fs");

const performdg = require("../../util/performance_data_generator");
const artefact_service = require("../../services/crud_artefact_service");
const intop_service = require("../../services/intop_service");
const perform_util = require("../../util/performance_util");

let createdArtefacts = [];
let resultsarray = [];

exports.command = 'performancetest'
exports.desc = ''
exports.builder = {}


//const nodecounts = [1,2,3,5,7,10, 20,30,50,70,100, 200, 300, 500,700,1000];
const nodecounts = [1,2];
const metadatacounts = [1,2];
const iterations = 3;

exports.handler = function (argv) {

    createArtefactMocks(metadatacounts).then(()=>{
        return perform_tests(nodecounts,metadatacounts,iterations)
    }).then(()=>{
        saveResultsToFiles();
    }).then(()=>{
        cleanUpArtefactMocks();
    })
};

function saveResultsToFiles(){
    let operations = ["check", "recommendation"];
    let testinstances = ["direct", "indirect"];

    let sortedResults = perform_util.sortResults(resultsarray, operations, testinstances, nodecounts, metadatacounts);


    if (!fs.existsSync(path)) {
        fs.mkdirSync("performance_results");
    }


    let promises = [];

    for(let o = 0; o<operations.length; o++){
        for(let t = 0; t< testinstances.length; t++){
            let instance = sortedResults[operations[o]][testinstances[t]];
            let perNodeCsvArray = perform_util.nodesSpreadsheetCsv(instance,nodecounts,metadatacounts);
            let perMetadataCsvArray = perform_util.metadataSpreadsheetCsv(instance,nodecounts,metadatacounts);
            promises.push(perform_util.toCsvFile(perNodeCsvArray, `performance_results/${operations[o]}_${testinstances[t]}_nodes.csv`));
            promises.push(perform_util.toCsvFile(perMetadataCsvArray, `performance_results/${operations[o]}_${testinstances[t]}_metadata.csv`));
        }
    }


    return Promise.all(promises);

}


function createArtefactMocks(metadatacounts){
    let max_metadatacount = Math.max.apply(Math, metadatacounts);
    //TODO add broker

    let artefactDirect = performdg.solution_artefact_direct(max_metadatacount);
    let artefactIndirect = performdg.solution_artefact_indirect(max_metadatacount);
    let artefactBroker = performdg.solution_artefact_broker();

    let promises= [];
    promises.push(artefact_service.createArtefact(artefactDirect).then((new_a)=>{createdArtefacts.push(new_a)}));
    promises.push(artefact_service.createArtefact(artefactIndirect).then((new_a)=>{createdArtefacts.push(new_a)}));
    promises.push(artefact_service.createArtefact(artefactBroker).then((new_a)=>{createdArtefacts.push(new_a)}));
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

function perform_tests(nodecounts, metadatacounts, iterations){
    return loopIterations(0,iterations,metadatacounts,nodecounts);
}

function loopIterations(i, iterations, metadatacounts, nodecounts){
    if(i<iterations){
        return loopMetadata(i,0,metadatacounts,nodecounts).then(()=>{
            return loopIterations(i+1, iterations,metadatacounts,nodecounts);
        });
    }
}


function loopMetadata(i, m, metadatacounts, nodecounts){
    if(m<metadatacounts.length){
        return loopNodes(i,m,0,nodecounts).then(()=>{
            return loopMetadata(i,m+1,metadatacounts,nodecounts);
        })
    }
}

function loopNodes(i, m, n, nodecounts){
    if(n<nodecounts.length){
        return innerloop(i,m,n).then(()=>{
            return loopNodes(i,m,n+1,nodecounts);
        });
    }
}

function innerloop(i,m,n){
    let direct_slice = performdg.direct_instance("direct",nodecounts[n], metadatacounts[m], performdg.metadata_parameters_ERROR());
    let indirect_slice = performdg.indirect_instance("indirect",nodecounts[n], metadatacounts[m], performdg.metadata_parameters_ERROR());

    return test(direct_slice, "direct", nodecounts[n], metadatacounts[m])
        .then(()=>{
            return test(indirect_slice, "indirect", nodecounts[n], metadatacounts[m]);
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