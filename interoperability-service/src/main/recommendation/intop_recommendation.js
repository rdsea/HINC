const intop_check = require('../check/intop_check');
const util = require('../util/slice_util');


exports.getRecommendations = function(slice, checkresults){

};

exports.getRecommendations = function(slice){
    let checkresults = intop_check.check(slice);
    return exports.getRecommendations(slice, checkresults);
};

exports.applyRecommendations = function(slice, checkresults){

};

exports.applyRecommendations = function(slice){

};



exports.testApplyWithFixedResources = function (slice, checkresults, resources) {
    return util.deepcopy(slice);
};
