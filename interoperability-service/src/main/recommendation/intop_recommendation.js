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

    for(let i = 0; i < checkresults.errors.length; i++) {
        //TODO search
        let resources = searchResources("");
        if(resources.length>0) {
            solveByAddition(checkresults.errors[i], slice, resources[0]);
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

function solveByAddition(error, slice, solution_resource){
    let source = slice.resources[error.cause.source.nodename];
    let dest = slice.resources[error.cause.target.nodename];
    util.sliceDisConnect(slice, source, dest);

    let intopName = "intop_" + solution_resource.name;

    intopName = util.sliceAddResource(slice, solution_resource, intopName);

    util.sliceConnectById(slice, error.cause.source.nodename, intopName, intopName + "1");
    util.sliceConnectById(slice, intopName, error.cause.target.nodename, intopName + "2");
}


function solveBySubstitution(error, slice, solution_resource){

}


function solveByReduction(error, slice, solution_resource){

}



exports.setTestMode = function (testMode, testResources) {
  test_mode = testMode;
  test_resources = testResources;
};


function searchResources(searchParameters){
    if(test_mode){
        return test_resources;
    }
    //TODO
    return [];
}