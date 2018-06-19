const intop_check = require('../check/intop_check');
const util = require('../util/slice_util');

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
    if(checkresults.errors.length === 0){
        return slice;
    }
    let queryMockIndex = 0;
    //for(let i = 0; i < checkresults.errors.length; i++) {
    while(checkresults.errors.length>0){
        queryMockIndex ++;
        //TODO search
        let solution_resource = searchResources("query" + queryMockIndex);
        let errorcount = checkresults.errors.length;
        if(solution_resource != null) {
            let broker = searchResources("broker");
            solveByAddition(checkresults.errors[0], slice, solution_resource, broker);
            checkresults = intop_check.checkSlice(slice);
        }

        if(checkresults.errors.length>=errorcount){
            break;
        }
        /* TODO: solution categorisation (Addition, Reduction, Substitution)
        if (checkresults.errors[i].impact.length > 0) {
            // can not be solved by reduction
        }else{
            solveBySubstitution()
        }
        */
    }

    //TODO for each problem, check which kind of problem it is (addition, reduction, substitution)
    //TODO solve problem by kind

    return slice;
};

function solveByAddition(error, slice, solution_resource, broker){
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

        let intopBroker = "intop_" + broker.name;
        intopBroker = util.sliceAddResource(slice, broker, intopBroker);

        let intopName = "intop_" + solution_resource.name;
        intopName = util.sliceAddResource(slice, solution_resource, intopName);


        util.sliceConnectById(slice, error.cause.source.nodename, intopBroker, intopName + "1");
        util.sliceConnectById(slice, intopBroker, intopName, intopName + "2");
        util.sliceConnectById(slice, intopName, error.cause.path[1].nodename, intopName + "3");
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
    if(test_mode){
        return test_resources[error];
    }
    //TODO
    return null;
}