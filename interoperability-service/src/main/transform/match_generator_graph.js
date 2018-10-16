exports.generateMatchObject = function(sourceNode, targetNode, isDirectConnection){
    //TODO check for duplicates
    return {source: sourceNode, target: targetNode, isDirect: isDirectConnection};
};