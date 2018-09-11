exports.generateMatchObject = function(sourceNode, targetNode, isDirectConnection){
    return {source: sourceNode, target: targetNode, isDirect: isDirectConnection};
};