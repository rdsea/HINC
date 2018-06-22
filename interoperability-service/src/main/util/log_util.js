exports.createCheckErrorLog = function (error){
    let log = {};
    log.nodes = error.cause.source.resource.name + "  -/->  " + error.cause.target.resource.name;
    log.metadata= error.cause.metadataInvolved;
    /*for(let i = 0; i < error.cause.metadataInvolved.length; i++){
        log.metadata += error.cause.metadataInvolved[i].key + " = " + error.cause.metadataInvolved[i].value + "\n";
    }*/

    log.path = "";
    for(let i = 0; i < error.cause.path.length; i ++){
        log.path += error.cause.path[i].resource.name + " -> ";
    }
    log.path += error.cause.target.resource.name;
    return log;
};

exports.createNoSolutionFoundLog = function(error){
    let log = {error: exports.createCheckErrorLog(error)};
    log.recommendation = {description: "no solution found", resource:"", path:log.error.path};
    return log;
};

exports.createRecommendationLog = function(error, source, solution_resource, dest){
    let log = {error: exports.createCheckErrorLog(error)};

    let recommendation = {};

    recommendation.description = "add " + solution_resource.name + " between " + source.name + " and " + dest.name;
    recommendation.resource = solution_resource;
    recommendation.path = source.name + " -> " + solution_resource.name + " -> " + dest.name;

    log.recommendation = recommendation;

    return log;
};