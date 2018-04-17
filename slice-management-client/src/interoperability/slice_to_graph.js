exports.sliceToGraph = function(slice){
    let unconnectedResources = slice;
    let pureOutputs = slice.filter(exports.hasNoInputs);
    let pureInputs = slice.filter(exports.hasNoOutputs);
    let inOuts = slice.filter(exports.hasBoth);


    let graph = {edges:[]};

    return graph;
};


exports.hasNoInputs = function(resource){
    return !resource.metadata.hasOwnProperty("input") || (resource.metadata.input.length === 0);
};

exports.hasNoOutputs = function (resource){
    return !resource.metadata.hasOwnProperty("output") || (resource.metadata.output.length === 0);
};

exports.hasBoth = function (resource) {
    return !exports.hasNoOutputs(resource) && !exports.hasNoInputs(resource);
};


function resourceById(id){
    return function(resource){ return resource.metadata.id === id;};
}