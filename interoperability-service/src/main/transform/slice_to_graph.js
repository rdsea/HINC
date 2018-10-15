
exports.sliceToGraph = function(slice){
    let nodeArray = Object.keys(slice.resources).map(function (key) { return {resource:slice.resources[key], nodename:key, marked:false}; });
    let nodes ={};

    for(let i = 0; i < nodeArray.length; i++){
        nodes[nodeArray[i].nodename] = nodeArray[i];
    }

    let edges = slice.connectivities;
    let startNodes = getStartNodes(nodes);

    return {nodes: nodes, edges: edges, startNodes: startNodes};
};


function getStartNodes(nodes){
    //select nodes without sources but with a targets
    let startnodes = [];
    Object.keys(nodes).forEach(function (key) {
        if (typeof nodes[key].resource.source === 'undefined' || nodes[key].resource.source.length === 0) {
            if (typeof nodes[key].resource.target !== 'undefined' && nodes[key].resource.target.length > 0) {
                startnodes.push(nodes[key]);
            }
        }
    });
    return startnodes;
}

exports.nextNodes = function (graph, node){
    let nextnodes = [];

    if(typeof node.resource.target === 'undefined'){
        return [];
    }

    for(let i = 0; i< node.resource.target.length; i++){
        let targetNode = graph.edges[node.resource.target[i]].out;
        if(typeof targetNode === "object" && targetNode.label){
            targetNode = targetNode.label;
        }
        nextnodes.push(graph.nodes[targetNode]);
    }


    return nextnodes;
};



exports.previousNodes = function (graph, node){
    let previousNodes = [];

    if(typeof node.resource.source === 'undefined'){
        return [];
    }

    for(let i = 0; i< node.resource.source.length; i++){
        let targetNode = graph.edges[node.resource.source[i]].in;
        if(typeof targetNode === "object" && targetNode.label){
            targetNode = targetNode.label;
        }
        previousNodes.push(graph.nodes[targetNode]);
    }


    return previousNodes;
};

exports.getUnmarkedNodes = function (graph) {
    let unmarkedNodes = [];
    Object.keys(graph.nodes).forEach(function (key) {
        if (graph.nodes[key].marked === false) {
            unmarkedNodes.push(graph.nodes[key]);
        }
    });
    return unmarkedNodes;
};