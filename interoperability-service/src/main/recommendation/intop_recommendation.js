const intop_check = require('../check/intop_check');
const util = require('../util/slice_util');
const MongoClient = require("mongodb").MongoClient;

//let mongodbUrl = "mongodb://test:rsihub1@ds161710.mlab.com:61710/recommendation_test";
let mongodb_config = {
    url: "mongodb://localhost:27017/recommendation_test",
    db: "recommendation_test",
    collection: "test"
};

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

        if(checkresults.errors.length === 0){
            resolve(slice);
        }
        let promises = [];
        for(let i = 0; i < checkresults.errors.length; i++) {
            promises.push(searchResources(checkresults.errors[i]).then(
                function (solution_resource) {
                    if(solution_resource != null) {
                        return solveByAddition(checkresults.errors[i], slice, solution_resource, checkresults);
                    }else{
                        resolve(slice);
                    }
                }, console.err
            ).then(function(){},console.err));
            // TODO: solution categorisation (Addition, Reduction, Substitution)

        }

        Promise.all(promises).then(function(){
            resolve(slice)});

        //TODO for each problem, check which kind of problem it is (addition, reduction, substitution)
        //TODO solve problem by kind
    });
};

function solveByAddition(error, slice, solution_resource, checkresults){
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

        //TODO check for M:N dependencies and
        //TODO check where transformation should be injected (between source/broker or broker/target)
        //TODO source/broker if: 1:N or M:N (coordinated)
        //TODO broker/target if: M:1 or M:N (coordinated)

        if(error.impact.length>0 && !error.cause.isDirect){
            let result = determineSolutionPosition(slice, error, source, solution_resource, dest, checkresults);

            if(result){
                source = result.newSource;
                dest = result.newDest;
            }
        }



        util.sliceDisConnect(slice, source, dest);
        reconnectResources(slice, [source, solution_resource, dest], intopName).then(resolve);

    });

}


function solveBySubstitution(error, slice, solution_resource, broker){

}


function solveByReduction(error, slice, solution_resource, broker){

}

function determineSolutionPosition(slice, error, source, solution_resource, dest, checkresults) {
    //CASE 0_2:  source -> dest, impact broker
    //CASE 0_5:  broker -> httpdest, impact mqttdest
    //        :  source -> broker, impact httpdest, mqttdest
    //CASE 0_8:  source -> dest, impact broker
    //CASE 0_9:  source -> dest2, impact broker,dest1
    //CASE 0_10:  source1 -> dest2, impact broker,dest2
    //         :  source2 -> dest1, impact broker,dest1

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
    //TODO add broker to the correct side of the transformer
    //CASE 1: source -> intop -> dest (all http)
    //CASE 2: source -> intop_broker -> dest

    //CASE 3: source -> intop -> broker -> dest
    //        source > broker > intop > broker > dest

    //CASE 4: source -> intop -> dest
    //        source -> broker -> intop -> broker -> dest

    return new Promise(function(resolve,reject) {
        addBrokers(slice, resourceArray).then(function (newResourceArray) {
            for (let i = 1; i < newResourceArray.length; i++) {
                //TODO connect by id
                let source = newResourceArray[i - 1];
                let dest = newResourceArray[i];
                util.sliceConnect(slice, newResourceArray[i - 1], newResourceArray[i], connectionName + i);
            }
            resolve();
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
                promises.push(searchBroker(protocol_name).then(function (broker) {
                    brokers[""+i] = broker;
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

function searchBroker(protocol_name){
    return new Promise(function (resolve, reject){
        let query = {"metadata.resource.type.prototype":"messagebroker", "metadata.resource.type.protocols.protocol_name":protocol_name};

        MongoClient.connect(mongodb_config.url, function(err, db) {
            if (err) return reject(err);
            let dbo = db.db(mongodb_config.db);
            dbo.collection(mongodb_config.collection).find(query).toArray(function(err, result) {
                if (err) throw err;
                db.close();
                resolve(result[0]);
            });
        });
    });
}


function searchResources(error){
    return new Promise(function (resolve, reject){
        let query = createQueryByExample(error);

        MongoClient.connect(mongodb_config.url, function(err, db) {
            if (err) return reject(err);
            let dbo = db.db(mongodb_config.db);
            dbo.collection(mongodb_config.collection).find(query).toArray(function(err, result) {
                if (err) throw err;
                db.close();
                resolve(result[0]);
            });
        });
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