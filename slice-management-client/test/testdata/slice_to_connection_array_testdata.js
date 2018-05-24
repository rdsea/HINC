


exports.emptyResource = function(id) {
    return {metadata: {id:id}};
};

exports.sliceConnectResources = function (source, destination, connectionMetadata, connectionId){
    //metadata connection (metadata of source and destination will match)
    let deepCopy_source = JSON.parse(JSON.stringify(connectionMetadata));
    let deepCopy_dest = JSON.parse(JSON.stringify(connectionMetadata));

    //metadata connection (metadata of source and destination will match)
    if(!source.metadata.hasOwnProperty("outputs")){
        source.metadata.outputs = [deepCopy_source];
    }else {
        source.metadata["outputs"].push(deepCopy_source);
    }
    if(!destination.metadata.hasOwnProperty("inputs")){
        destination.metadata.inputs = [deepCopy_dest];
    }else {
        destination.metadata["inputs"].push(deepCopy_dest);
    }

    //slice connection (connection as defined in the slice)
    if(!source.hasOwnProperty("target")){
        source.target = [connectionId];
    }else {
        source["target"].push(connectionId);
    }
    if(!destination.hasOwnProperty("source")){
        destination.source = [connectionId];
    }else {
        destination["source"].push(connectionId);
    }

};



exports.testdata_twoConnectedResources = function () {
    let r1 = exports.emptyResource("id_r1");
    let r2 = exports.emptyResource("id_r2");

    exports.sliceConnectResources(r1, r2, {protocol:"mqtt"}, "connectionID12");

    return {resources:{"r1":r1,"r2":r2}};
};

exports.testdata_twoIndependentResources = function () {
    let r1 = exports.emptyResource("id_r1");
    let r2 = exports.emptyResource("id_r2");
    return {resources:{"r1":r1,"r2":r2}};
};

exports.testdata_circle_twoResources = function () {
    let r1 = exports.emptyResource("id_r1");
    let r2 = exports.emptyResource("id_r2");

    exports.sliceConnectResources(r1, r2, {protocol:"mqtt"}, "connectionID12");
    exports.sliceConnectResources(r2, r1, {protocol:"mqtt"}, "connectionID21");

    return {resources:{"r1":r1,"r2":r2}};
};

exports.testdata_diamond_fourResources = function () {
    let r1 = exports.emptyResource("id_r1");
    let r2 = exports.emptyResource("id_r2");
    let r3 = exports.emptyResource("id_r3");
    let r4 = exports.emptyResource("id_r4");

    exports.sliceConnectResources(r1, r2, {protocol:"mqtt"}, "connectionID12");
    exports.sliceConnectResources(r1, r3, {protocol:"mqtt"}, "connectionID13");
    exports.sliceConnectResources(r2, r4, {protocol:"mqtt"}, "connectionID24");
    exports.sliceConnectResources(r3, r4, {protocol:"mqtt"}, "connectionID34");

    return {resources:{"r1":r1,"r2":r2, "r3":r3, "r4":r4}};
};

exports.testdata_diamondWithCircle_fourResources_oneUnconnectedResource = function () {
    let r1 = exports.emptyResource("id_r1");
    let r2 = exports.emptyResource("id_r2");
    let r3 = exports.emptyResource("id_r3");
    let r4 = exports.emptyResource("id_r4");

    exports.sliceConnectResources(r1, r2, {protocol:"mqtt"}, "connectionID12");
    exports.sliceConnectResources(r1, r3, {protocol:"mqtt"}, "connectionID13");
    exports.sliceConnectResources(r2, r4, {protocol:"mqtt"}, "connectionID24");
    exports.sliceConnectResources(r3, r4, {protocol:"mqtt"}, "connectionID34");

    exports.sliceConnectResources(r2, r3, {protocol:"mqtt"}, "connectionID23");
    exports.sliceConnectResources(r3, r2, {protocol:"mqtt"}, "connectionID32");

    let r5 = exports.emptyResource("id_r5");

    return {resources:{"r1":r1,"r2":r2, "r3":r3, "r4":r4, "r5":r5}};
};

//TODO testcase with unconnected source/target

//TODO messagebroker example
//TODO test cases with open inputs/outputs
