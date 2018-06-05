
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
        nextnodes.push(graph.nodes[targetNode]);
    }


    return nextnodes;
};