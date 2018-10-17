const graph_util = require('./slice_to_graph');
const log_util = require('../util/log_util');


exports.generateErrorObject = function(sourceNode, targetNode, checkErrors, isDirectConnection, path, graph){
    //TODO check for duplicates
    let error = {};
    let cause = {source:sourceNode, target: targetNode, isDirect:isDirectConnection, path:path};

    cause.metadataInvolved = checkErrors.errors;
    error.cause = cause;
    let dependentNodes = [];
    getDependentNodes(sourceNode, sourceNode, targetNode, graph, dependentNodes, true);

    error.impact = dependentNodes;

    error.log = log_util.createCheckErrorLog(error);

    return error;
};


function getDependentNodes(startNode, currentNode, targetNode, graph, dependentNodes, directConnection){
    let nextNodes = graph_util.nextNodes(graph, currentNode);

    for(let i = 0; i<nextNodes.length;i++){
        if(dependentNodesContains(dependentNodes, nextNodes[i])){
            continue;
        }
        if(nextNodes[i] !== targetNode){
            dependentNodes.push({node:nextNodes[i], direct:directConnection});
        }

        if(nextNodes[i].resource.metadata.resource.category.startsWith("network_function")){
            getDependentNodes(startNode, nextNodes[i], targetNode, graph, dependentNodes, false);
        }
    }
}

function dependentNodesContains(dependentNodes, node){
    for(let i = 0; i<dependentNodes.length; i++){
        if(dependentNodes[i].node === node){
            return true;
        }
    }
    return false;
}
