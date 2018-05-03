exports.sliceToConnectionList = function(slice){
    let resources = slice.resources;
    let connections = [];

    resources.forEach(addConnection(resources, connections));

    return connections;
};



function resourceBySourceId(connectionId){
    return function (resource) {
        if(!resource.source){
            return false;
        }
        for(let i = 0; i<resource.source.length; i++){
            if(resource.source[i] === connectionId){
                return true;
            }
        }
        return false;
    }
}

function addConnection(resources, connections) {
    return function (item) {

        if(item.target && item.target.length>0) {

            for(let i = 0; i < item.target.length; i++) {

                let targetResources = resources.filter(resourceBySourceId(item.target[i]));

                for(let j = 0; j < targetResources.length; j++){

                    let arrayelement = {source:{}, target:{}, connectionId:{}};
                    arrayelement.source = item;
                    arrayelement.connectionId = item.target[i];

                    arrayelement.target = targetResources[j];
                    connections.push(arrayelement);
                }
            }
        }
    };
}