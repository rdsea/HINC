exports.resourceCount = function(slice){
    return Object.keys(slice.resources).length;
};

exports.sliceAddResource = function(slice, resource, name){
    let id = getResourceId(slice, name);
    slice.resources[id]=resource;
    return id;
};

exports.isConnected = function(slice, source, destination){
    let connectivityName = null;


    //one element in source.target == one element dest.target
    for(let i = 0; i < source.target.length; i++){
        if(destination.source.indexOf(source.target[i])>-1){
            connectivityName = source.target[i];
            break;
        }
    }

    if(connectivityName === null){
        return false;
    }

    //slice[connectivity.in].name == source.name
    if(slice.resources[slice.connectivities[connectivityName].in].name !== source.name){
        return false
    }

    if(slice.resources[slice.connectivities[connectivityName].out].name !== destination.name){
        return false
    }

    return true;

};

exports.contains = function(slice, resourceName){
    return Object.keys(slice.resources).map(function(r){return slice.resources[r].name}).indexOf(resourceName)>-1;
};

exports.deepcopy = function(obj){
    return JSON.parse(JSON.stringify(obj));
};



exports.sliceConnect = function(slice, source, dest, connectivityname){
    let connectivityId = getConnectivityId(slice, connectivityname);

    let sourceId = exports.getResourceIdByName(slice, source.name);
    let destId = exports.getResourceIdByName(slice, dest.name);

    slice.connectivities[connectivityId]={in:sourceId, out:destId};
    source.target.push(connectivityId);
    dest.source.push(connectivityId);
};

exports.sliceConnectById = function(slice, sourceId, destId, connectivityname){
    let source = slice.resources[sourceId];
    let dest = slice.resources[destId];
    slice.connectivities[connectivityname]={in:sourceId, out:destId};
    source.target.push(connectivityname);
    dest.source.push(connectivityname);
};


exports.sliceDisConnectWithName = function(slice, source, dest, connectivityname){
    arrayRemove(dest.source, connectivityname);
    arrayRemove(source.target, connectivityname);
    delete slice.connectivities[connectivityname];
};


exports.sliceDisConnect = function(slice, source, dest){
    let connectivityname = null;


    //one element in source.target == one element dest.target
    for(let i = 0; i < source.target.length; i++){
        if(dest.source.indexOf(source.target[i])>-1){
            connectivityname = source.target[i];
            break;
        }
    }

    arrayRemove(dest.source, connectivityname);
    arrayRemove(source.target, connectivityname);
    delete slice.connectivities[connectivityname];
};

exports.getResourceIdByName = function(slice, resourceName){
    return Object.keys(slice.resources).filter(function filterFunction(key){
        return slice.resources[key].name === resourceName
    }).pop();
};

function getResourceId(slice, baseId){
    if(slice.resources.hasOwnProperty(baseId)){
        let i = 1;
        while(slice.resources.hasOwnProperty(baseId+"_"+i)){
            i++;
        }
        return baseId+"_"+i;
    }else{
        return baseId
    }
}

function getConnectivityId(slice, baseId){
    if(slice.connectivities.hasOwnProperty(baseId)){
        let i = 1;
        while(slice.connectivities.hasOwnProperty(baseId+"_"+i)){
            i++;
        }
        return baseId+"_"+i;
    }else{
        return baseId
    }
}


function arrayRemove(array, object){
    let index = array.indexOf(object);
    if (index > -1) {
        array.splice(index, 1);
    }
}