const intop_check = require('../check/intop_check');
const util = require('../util/slice_util');
const log_util = require('../util/log_util');
const request = require('request-promise');
const config = require('../../config');

const SEARCH_SOFTWARE_ARTEFACT = config.SOFTWARE_ARTEFACT_URI + config.SEARCH_ARTEFACTS;

const SEARCH_RESOURCES = config.GLOBAL_MANAGEMENT_URI + config.SEARCH_RESOURCES;


exports.getRecommendationsWithoutCheck = function(slice, checkresults){
    let testslice = util.deepcopy(slice);
    return exports.applyRecommendationsWithoutCheck(testslice, checkresults);
};

exports.getRecommendations = function(slice){
    let checkresults = intop_check.checkSlice(slice);
    return exports.getRecommendationsWithoutCheck(slice, checkresults);
};

exports.applyRecommendations = function(slice){
    let checkresults = intop_check.checkSlice(slice);
    return exports.applyRecommendationsWithoutCheck(slice, checkresults);
};


exports.applyRecommendationsWithoutCheck = function(slice, checkresults){
    return new Promise(function(resolve, reject){

        let logs = [];
        recursiveSolve(slice,checkresults, logs).then(function (slice){
            let result = {slice:slice, logs:logs};
            resolve(result);
        }).catch((error)=>{
            console.log(error);
        });

    });
};

function recursiveSolve(slice, checkresults, logs){
    return new Promise(function (resolve, reject) {
        if(checkresults.errors.length === 0){
            resolve(slice)
        }else {

            searchResources(checkresults.errors[0])
                .then(function (solution_resources) {
                    if (solution_resources.length > 0) {
                        return solveByAddition(checkresults.errors[0], slice, solution_resources[0], checkresults, logs);
                        //TODO for each problem, check which kind of problem it is (addition, reduction, substitution)
                        //TODO solve problem by kind
                    } else {
                        //TODO add log information that no resource has been found for this error

                        logs.push(log_util.createNoSolutionFoundLog(checkresults.errors[0]));
                        resolve(slice);
                    }
                }, reject)
                .then(function (slice) {
                    checkresults = intop_check.checkSlice(slice);
                    return recursiveSolve(slice, checkresults, logs);
                }, reject)
                .then(function (slice) {
                    resolve(slice);
                }, reject);
        }
    })

}

function solveByAddition(error, slice, solution_resource, checkresults, logs){
    return new Promise(function(resolve, reject) {
        let intopName = "intop_" + solution_resource.name;
        intopName = util.sliceAddResource(slice, solution_resource, intopName);


        let source = slice.resources[error.cause.source.nodename];
        let dest = slice.resources[error.cause.target.nodename];

        if (error.cause.isDirect === true) {
            dest = slice.resources[error.cause.target.nodename];
        } else {
            dest = slice.resources[error.cause.path[1].nodename];
        }

        if(error.impact.length>0 && !error.cause.isDirect){
            //check where solution_resource should be added (between source/broker or broker/target)
            let result = determineSolutionPosition(slice, error, source, solution_resource, dest, checkresults);

            if(result){
                source = result.newSource;
                dest = result.newDest;
            }
        }

        logs.push(log_util.createRecommendationLog(error, source, solution_resource, dest));


        util.sliceDisConnect(slice, source, dest);
        reconnectResources(slice, [source, solution_resource, dest], intopName).then(
            function(slice){
                resolve(slice)
            });
    });
}


function solveBySubstitution(error, slice, solution_resource, checkresults, logs){

}


function solveByReduction(error, slice, solution_resource, checkresults, logs){

}

function determineSolutionPosition(slice, error, source, solution_resource, dest, checkresults) {
    //check where solution_resource should be added (between source/broker or broker/target)

    let target = slice.resources[error.cause.target.nodename];
    //if source || dest broker --> ignore
    if(isBroker(source) || isBroker(target)){
        return;
    }


    let addTransformationAtDest = false;

    for(let i = 0; i< error.impact.length; i++){
        if(isBroker(error.impact[i].node.resource)){
            continue;
        }
        //if there is one dest that matches source, add transformation to dest
        if(isMatching(source, error.impact[i].node.resource, checkresults)){
            addTransformationAtDest = true;
            break;
        }
    }
    //if impact broker --> ignore
    //if impact between source/dest -> add transformation to dest

    if(addTransformationAtDest === true){
        source = slice.resources[error.cause.path[error.cause.path.length-1].nodename];
        dest = slice.resources[error.cause.target.nodename];

        return {newSource: source, newDest:dest};
    }
}

function isMatching(source, dest, checkresults){
    for(let i = 0; i<checkresults.matches.length; i++){
        if(checkresults.matches[i].source.resource === source && checkresults.matches[i].target.resource === dest){
            return true;
        }
    }
    return false;

}

function isBroker(resource){
    return (resource.metadata.resource.type.prototype === "messagebroker");
}

function reconnectResources(slice, resourceArray, connectionName){
    //add broker to the correct side of the transformer

    return new Promise(function(resolve,reject) {
        addBrokers(slice, resourceArray).then(function (newResourceArray) {
            for (let i = 1; i < newResourceArray.length; i++) {
                //TODO connect by id
                util.sliceConnect(slice, newResourceArray[i - 1], newResourceArray[i], connectionName + i);
            }
            resolve(slice);
        });
    });
}

function addBrokers(slice, resourceArray){
    return new Promise(function(resolve, reject){

        let newResourceArray = [];
        let promises = [];
        let brokers = {};

        for(let i = 1; i<resourceArray.length; i++){
            let source = resourceArray[i-1];
            let dest = resourceArray[i];

            let protocol_name = brokerNeeded(source,dest);

            if(protocol_name){
                promises.push(searchBrokers(protocol_name).then(function (found_brokers) {
                    brokers[""+i] = found_brokers[0];
                }));
            }
        }

        Promise.all(promises).then(function(){
            for(let i = 1; i<=resourceArray.length; i++){
                newResourceArray.push(resourceArray[i-1]);
                if(brokers[""+i]){
                    let newBroker = util.deepcopy(brokers[""+i]);
                    util.sliceAddResource(slice, newBroker, newBroker.name);
                    newResourceArray.push(newBroker);
                }
            }
            resolve(newResourceArray);
        });

    });

}

function brokerNeeded(source, target){
    if(isBroker(source) || isBroker(target)){
        return;
    }

    let sourceOutput = source.metadata.outputs[0];
    let targetInput = target.metadata.inputs[0];
    //TODO use correct input/output


    if(sourceOutput.protocol.protocol_name === targetInput.protocol.protocol_name) {
        if(arrayContains(["mqtt", "mqtts", "amqp", "amqps"],sourceOutput.protocol.protocol_name)){
            if(!isBroker(source) && !isBroker(target)) {
                return sourceOutput.protocol.protocol_name;
            }
        }
    }
}

function searchBrokers(protocol_name){
    let query = {"metadata.resource.type.prototype":"messagebroker", "metadata.resource.type.protocols.protocol_name":protocol_name};
    return exports.queryServices(query);
}

function searchResources(error){
    let query = createQueryByExample(error);
    return exports.queryServices(query);
}

exports.queryServices = function(query){
    return new Promise(function (resolve, reject){
        let jsonQuery = JSON.stringify(query, 0, null);
        let allResources = [];
        let promises = [];

        promises.push(request.post({url: SEARCH_RESOURCES, body: jsonQuery}).then((result)=>{
            console.log(result);
            let resources = JSON.parse(result);
            allResources = allResources.concat(resources);
        }));

        promises.push(request.post({url: SEARCH_SOFTWARE_ARTEFACT, body: jsonQuery}).then((result)=>{
            console.log(result);
            let resources = JSON.parse(result);
            allResources = allResources.concat(resources);
        }));

        return Promise.all(promises).then(()=>{
            resolve(allResources);
        }).catch((error)=>{
            reject(error);
        })
    });
}

function createQueryByExample(error){
    let example = {};

    for(let i = 0; i < error.cause.metadataInvolved.length; i++){
        example[error.cause.metadataInvolved[i].key]=error.cause.metadataInvolved[i].value;
    }

    if(!((example["metadata.inputs.protocol.protocol_name"] && example["metadata.outputs.protocol.protocol_name"])
        || example["metadata.resource.type.prototype"]==="messagebroker") ){
        example["metadata.outputs.protocol.protocol_name"] = error.cause.target.resource.metadata.inputs[0].protocol.protocol_name;
        example["metadata.inputs.protocol.protocol_name"] = error.cause.source.resource.metadata.outputs[0].protocol.protocol_name;
    }

    // example = {"metadata.outputs.protocol.protocol_name":"mqtt", "metadata.inputs.protocol.protocol_name":"http"};
    // example["metadata.outputs.protocol.protocol_name"]="mqtt";
    // example["metadata.inputs.protocol.protocol_name"]="http";

    return example;
}


function arrayContains(array, value){
    return array.indexOf(value)>-1;
}