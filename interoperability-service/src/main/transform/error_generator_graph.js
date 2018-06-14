const graph_util = require('./slice_to_graph');


exports.generateErrorObject = function(sourceNode, targetNode, checkErrors, isDirectConnection, graph){
    let error = {};
    let cause = {source:sourceNode, target: targetNode, isDirect:isDirectConnection};

    cause.metadataInvolved = checkErrors.errors;
    error.cause = cause;
    let dependentNodes = [];
    getDependentNodes(sourceNode, sourceNode, targetNode, graph, dependentNodes, true);

    error.impact = dependentNodes;

    return error;
};


function getDependentNodes(startNode, currentNode, targetNode, graph, dependentNodes, directConnection){
    let nextNodes = graph_util.nextNodes(graph, currentNode);

    for(let i = 0; i<nextNodes.length;i++){
        if(nextNodes[i] !== targetNode){
            dependentNodes.push({node:nextNodes[i], direct:directConnection});
        }

        if(nextNodes[i].resource.metadata.resource.category === "network_function"){
            getDependentNodes(startNode, nextNodes[i], targetNode, graph, dependentNodes, false);
        }
    }

}