const intop_check = require('../check/intop_check');
const util = require('../util/slice_util');
const MongoClient = require("mongodb").MongoClient;

//let mongodbUrl = "mongodb://test:rsihub1@ds161710.mlab.com:61710/recommendation_test";
let mongodbUrl = "mongodb://localhost:27017/recommendation_test";

let test_mode = false;
let test_resources = [];

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
        let queryMockIndex = 0;
        let promises = [];
        for(let i = 0; i < checkresults.errors.length; i++) {
            //while(checkresults.errors.length>0){
            queryMockIndex ++;
            //TODO search
            promises.push(searchResources(checkresults.errors[i]).then(function (solution_resource) {
                if(solution_resource != null) {
                    solveByAddition(checkresults.errors[i], slice, solution_resource);
                    //checkresults = intop_check.checkSlice(slice);
                }
            }, console.err));
            /*if(checkresults.errors.length>=errorcount){
                break;
            }*/
            /* TODO: solution categorisation (Addition, Reduction, Substitution)
            if (checkresults.errors[i].impact.length > 0) {
                // can not be solved by reduction
            }else{
                solveBySubstitution()
            }
            */
        }

        Promise.all(promises).then(function(){resolve(slice)});

        //TODO for each problem, check which kind of problem it is (addition, reduction, substitution)
        //TODO solve problem by kind
    });
};

function solveByAddition(error, slice, solution_resource){
    if(error.cause.isDirect) {
        //if its a structural problem or protocol issue, add broker
        //otherwise there should not be a broker involved --> add transformer
        //TODO direct add broker
        let source = slice.resources[error.cause.source.nodename];
        let dest = slice.resources[error.cause.target.nodename];
        util.sliceDisConnect(slice, source, dest);

        let intopName = "intop_" + solution_resource.name;

        intopName = util.sliceAddResource(slice, solution_resource, intopName);

        util.sliceConnectById(slice, error.cause.source.nodename, intopName, intopName + "1");
        util.sliceConnectById(slice, intopName, error.cause.target.nodename, intopName + "2");
    }else{
        //if direct, check for M:N dependencies and add broker to the correct side of the transformer

        let source = slice.resources[error.cause.source.nodename];
        let origbroker = slice.resources[error.cause.path[1].nodename];
        let target = slice.resources[error.cause.target.nodename];
        //TODO check where transformation should be injected (between source/broker or broker/target)
        //TODO source/broker if: 1:N or M:N (coordinated)
        //TODO broker/target if: M:1 or M:N (coordinated)

        util.sliceDisConnect(slice, source, origbroker);

        //TODO add broker
        /*let intopBroker = "intop_" + broker.name;
        intopBroker = util.sliceAddResource(slice, broker, intopBroker);*/

        let intopName = "intop_" + solution_resource.name;
        intopName = util.sliceAddResource(slice, solution_resource, intopName);

        util.sliceConnectById(slice, error.cause.source.nodename, intopName, intopName + "1");
        util.sliceConnectById(slice, intopName, error.cause.path[1].nodename, intopName + "2");

        /* OLD
        util.sliceConnectById(slice, error.cause.source.nodename, intopBroker, intopName + "1");
        util.sliceConnectById(slice, intopBroker, intopName, intopName + "2");
        util.sliceConnectById(slice, intopName, error.cause.path[1].nodename, intopName + "3");*/
    }
}


function solveBySubstitution(error, slice, solution_resource, broker){

}


function solveByReduction(error, slice, solution_resource, broker){

}



exports.setTestMode = function (testMode, testResources) {
  test_mode = testMode;
  test_resources = testResources;
};

function searchResources(error){
    return new Promise(function (resolve, reject){
        //TODO testmode/productionmode
        let query = createQueryByExample(error);

        MongoClient.connect(mongodbUrl, function(err, db) {
            if (err) return reject(err);
            let dbo = db.db("recommendation_test");
            dbo.collection("test").find(query).toArray(function(err, result) {
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

    if(!(example["metadata.inputs.protocol.protocol_name"] && example["metadata.outputs.protocol.protocol_name"]) ){
        example["metadata.outputs.protocol.protocol_name"] = error.cause.target.resource.metadata.inputs[0].protocol.protocol_name;
        example["metadata.inputs.protocol.protocol_name"] = error.cause.source.resource.metadata.outputs[0].protocol.protocol_name;
    }

    // example["metadata.outputs"+error.cause.metadataInvolved[0]] = error.cause.source.resource.metadata.outputs[0][error.cause.metadataInvolved[0]];
    // example["metadata.inputs"+error.cause.metadataInvolved[0]] = error.cause.target.resource.metadata.inputs[0][error.cause.metadataInvolved[0]];

    // example = {"metadata.outputs.protocol.protocol_name":"mqtt", "metadata.inputs.protocol.protocol_name":"http"};
    // example["metadata.outputs.protocol.protocol_name"]="mqtt";
    // example["metadata.inputs.protocol.protocol_name"]="http";

    return example;
}