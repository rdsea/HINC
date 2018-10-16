exports.createCheckErrorLog = function (error){
    let log = {};

    let source_nodename = error.cause.source.resource.name;
    let target_nodename = error.cause.target.resource.name;

    if(source_nodename === undefined){
        source_nodename = error.cause.source.nodename;
    }
    if(target_nodename === undefined){
        target_nodename = error.cause.target.nodename;
    }

    log.nodes = source_nodename + "  -/->  " + target_nodename;
    log.metadata= error.cause.metadataInvolved;
    /*for(let i = 0; i < error.cause.metadataInvolved.length; i++){
        log.metadata += error.cause.metadataInvolved[i].key + " = " + error.cause.metadataInvolved[i].value + "\n";
    }*/

    log.path = "";
    for(let i = 0; i < error.cause.path.length; i ++){
        let path_nodename = error.cause.path[i].resource.name;
        if(path_nodename === undefined){
            path_nodename = error.cause.path[i].nodename;
        }
        log.path += path_nodename + " -> ";
    }
    log.path += target_nodename;
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