const request = require('request');
const config = require("../../config");
const path = require("path");
const prompt =require('inquirer').createPromptModule();
const fs = require("fs");

const performdg = require("../../util/performance_data_generator");
const artefact_service = require("../../services/crud_artefact_service");
const intop_service = require("../../services/intop_service");

let createdArtefacts = [];
/*let testresults = {
    recommendation:{
        indirect:{

        },
        direct:{

        }
    ,
    check:{
        indirect:{

        },
        direct:{

        }
    }
};*/
let resultsarray = [];

exports.command = 'performancetest <nodecount> <metadatacount> <iterations>'
exports.desc = ''
exports.builder = {}

exports.handler = function (argv) {

    perform_tests(4,3,2).then((data)=>{
        console.log("after perform" + data);
        let i = 1;
    });


    /*let filepath = path.resolve(argv.file);
    let s;
    try {
        s = require(filepath);
    }catch (e) {
        throw new Error(`could not find slice for file ${argv.file}, please try again`);
    }

    let jsonObj = {slice:s};
    let requestBody = { json: true, body: jsonObj };
    let requestUri = config.intop_service_uri + "/interoperability/recommendation";

    request.post(requestUri, requestBody , (err, res, body) => {
        if (err) {
            console.error("Error connecting to the interoperability service. Please check the http uri of the interoperability service " +
                "(" + config.intop_service_uri + "). Detailed error message:")
            console.error(err);
            return;
        }

        if(body.logs){

            if(body.logs.length>0) {
                printRecommendationResults(body).then(()=>{

                let questions = [
                    {
                        name: 'overwriteSlice',
                        message: 'Should the slice be updated with the recommendation?',
                        type: "confirm",
                        default: false
                    }
                ];

                prompt(questions).then((ans)=>{
                    if(ans.overwriteSlice){
                        overwriteFile(filepath, JSON.stringify(body.slice, null, 2), function (err) {
                            if (err) {
                                return console.log("Error writing file: " + err);
                            }
                        })
                    }
                });
                });

            }else{
                console.info("No interoperability issues detected")
            }
        }else{
            console.error(body);
        }
    });*/
};

function printRecommendationResults(body){
    return new Promise((resolve, reject) => {



    console.info("Interoperability recommendation results: \n");
    if(body.logs.length>0) {
        for (let i = 0; i < body.logs.length; i++) {
            console.info((i+1) + ": \tError: \n\t\t\t"+ body.logs[i].error.nodes);
            console.info("\tRecommendation: \n\t\t\t"+ body.logs[i].recommendation.description);
            console.info("\tResource: \n\t\t\t"+ JSON.stringify(body.logs[i].recommendation.resource));
            console.info("\tNew Path: \n\t\t\t"+ body.logs[i].recommendation.path);
            console.info("");
        }
    }

    if(body.time){
        const NS_PER_SEC = 1e9;
        const MS_PER_NS = 1e-6

        console.log("Performance: ");
        let diff = body.time;
        console.log(`Benchmark took ${diff[0] * NS_PER_SEC + diff[1]} nanoseconds`);
        console.log(`Benchmark took ${ (diff[0] * NS_PER_SEC + diff[1])  * MS_PER_NS } milliseconds`);
    }

    let questions = [
        {
            name: 'showSlice',
            message: 'Print the resulting slice?',
            type: "confirm",
            default: false
        }
    ];

        prompt(questions).then((ans)=>{
            if(ans.showSlice) {
                console.info("Slice with recommendations:");
                console.info(JSON.stringify(body.slice, null, 2));
            }

            resolve();
        });
    });

}

function overwriteFile(filepath, content, callback){
    fs.truncate(filepath, 0, function() {
        fs.writeFile(filepath, content, callback);
    });
}

/*clearResults();

    let max_metadata = 10;*/

/*for(let nodenumber = 1; nodenumber<=10; nodenumber=nodenumber*10){
    for(let metadatanumber = 1; metadatanumber<=max_metadata; metadatanumber=metadatanumber*10){
        let slice1 = direct_instance("direct_instance", nodenumber, metadatanumber, metadata_parameters_OK());
        let slice2 = direct_instance("direct_instance", nodenumber,metadatanumber, metadata_parameters_ERROR());
        let slice3 = indirect_instance("indirect_instance", nodenumber,metadatanumber, metadata_parameters_OK());
        let slice4 = indirect_instance("indirect_instance", nodenumber,metadatanumber, metadata_parameters_ERROR());

        create_file(slice1, slice1.sliceId);
        create_file(slice2, slice2.sliceId);
        create_file(slice3, slice3.sliceId);
        create_file(slice4, slice4.sliceId);
    }
}*/

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
    /*if(testresults[operation]===undefined){
        testresults[operation] = {}
    }
    testresults[`${operation}.${instancename}`] = {};*/

    if(body.time) {
        const NS_PER_SEC = 1e9;
        const MS_PER_NS = 1e-6
        let diff = body.time;

        /*console.log("Performance: ");
        console.log(`Benchmark took ${diff[0] * NS_PER_SEC + diff[1]} nanoseconds`);
        console.log(`Benchmark took ${(diff[0] * NS_PER_SEC + diff[1]) * MS_PER_NS} milliseconds`);*/
        resultsarray.push({
            operation: operation,
            instancename: instancename,
            nodecount: nodecount,
            metadatacount: metadatacount,
            time_ms:((diff[0] * NS_PER_SEC + diff[1]) * MS_PER_NS)
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
    return loopIterations(0,2,3,4);
}

function loopIterations(i, iterations, metadatacount, nodecount){
    if(i<iterations){
        return loopMetadata(i,0,metadatacount,nodecount).then(()=>{
            return loopIterations(i+1, iterations,metadatacount,nodecount);
        });
    }
}


function loopMetadata(i, m, metadatacount, nodecount){
    if(m<metadatacount){
        return loopNodes(i,m,0,nodecount).then(()=>{
            return loopMetadata(i,m+1,metadatacount,nodecount);
        })
    }
}

function loopNodes(i, m, n, nodecount){
    if(n<nodecount){
        return innerloop(n,m,i).then(()=>{
            return loopNodes(i,m,n+1,nodecount);
        });
    }
}

function innerloop(nodecount, metadatacount, iterations){
    console.log(`i: ${iterations}  m:${metadatacount}  n:${nodecount}`);

    let slice1 = performdg.direct_instance("direct_instance",nodecount, metadatacount, performdg.metadata_parameters_OK());
    let slice3 = performdg.indirect_instance("indirect_instance",nodecount, metadatacount, performdg.metadata_parameters_OK());


    let slice2 = performdg.direct_instance("direct_instance",nodecount, metadatacount, performdg.metadata_parameters_ERROR());
    let slice4 = performdg.indirect_instance("indirect_instance",nodecount, metadatacount, performdg.metadata_parameters_ERROR());

    return test(slice1, "direct", nodecount, metadatacount)
        .then(()=>{
            return test(slice3, "indirect", nodecount, metadatacount);
        })
        .then(()=>{
            return test(slice2, "direct", nodecount, metadatacount);
        })
        .then(()=>{
            return test(slice4, "indirect", nodecount, metadatacount);
        })
}


function test(slice, instancename, nodecount, metadatacount){
    return intop_service.check(slice, 300000).then((body)=>{
        return saveResult("check", instancename, nodecount, metadatacount, body);
    })/*.then(()=>{
        return intop_service.recommendation(slice, 300000);
    }).then((body)=>{
        return saveResult("recommendation", instancename, nodecount, metadatacount, body);
    });*/

}