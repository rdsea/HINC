


exports.emptyResource = function(id) {
    return {metadata: {id:id}};
};

exports.sliceConnectResources = function (source, destination, connectionMetadata){
    if(!source.metadata.hasOwnProperty("output")){
        source.metadata.output = [connectionMetadata];
    }else {
        source.metadata["output"].push(connectionMetadata);
    }
    if(!destination.metadata.hasOwnProperty("input")){
        destination.metadata.input = [connectionMetadata];
    }else {
        destination.metadata["input"].push(connectionMetadata);
    }
};



exports.testdata_twoConnectedResources = function () {
    let r1 = exports.emptyResource("r1");
    let r2 = exports.emptyResource("r2");

    exports.sliceConnectResources(r1, r2, {protocol:"mqtt"});

    return [r1,r2];
};

exports.testdata_twoIndependentResources = function () {
    let r1 = exports.emptyResource("r1");
    let r2 = exports.emptyResource("r2");
    return [r1,r2];
};

exports.testdata_circle_twoResources = function () {
    let r1 = exports.emptyResource("r1");
    let r2 = exports.emptyResource("r2");

    exports.sliceConnectResources(r1, r2, {protocol:"mqtt"});
    exports.sliceConnectResources(r2, r1, {protocol:"mqtt"});

    return [r1,r2];
};

exports.testdata_diamond_fourResources = function () {
    let r1 = exports.emptyResource("r1");
    let r2 = exports.emptyResource("r2");
    let r3 = exports.emptyResource("r3");
    let r4 = exports.emptyResource("r4");

    exports.sliceConnectResources(r1, r2, {protocol:"mqtt"});
    exports.sliceConnectResources(r1, r3, {protocol:"mqtt"});
    exports.sliceConnectResources(r2, r4, {protocol:"mqtt"});
    exports.sliceConnectResources(r3, r4, {protocol:"mqtt"});

    return [r1,r2,r3,r4];
};

exports.testdata_diamond_fourResources_shuffled = function () {
    return shuffle(exports.testdata_diamond_fourResources());
};

exports.testdata_diamondWithCircle_fourResources_oneUnconnectedResource = function () {
    let r1 = exports.emptyResource("r1");
    let r2 = exports.emptyResource("r2");
    let r3 = exports.emptyResource("r3");
    let r4 = exports.emptyResource("r4");

    exports.sliceConnectResources(r1, r2, {protocol:"mqtt"});
    exports.sliceConnectResources(r1, r3, {protocol:"mqtt"});
    exports.sliceConnectResources(r2, r4, {protocol:"mqtt"});
    exports.sliceConnectResources(r3, r4, {protocol:"mqtt"});

    exports.sliceConnectResources(r2, r3, {protocol:"mqtt"});
    exports.sliceConnectResources(r3, r2, {protocol:"mqtt"});

    let r5 = exports.emptyResource("r5");

    return [r1,r2,r3,r4,r5];
};

//TODO messagebroker example
//TODO test cases with open inputs/outputs

/**
 * Shuffles array in place.
 * @param {Array} a items An array containing the items.
 */
function shuffle(a) {
    var j, x, i;
    for (i = a.length - 1; i > 0; i--) {
        j = Math.floor(Math.random() * (i + 1));
        x = a[i];
        a[i] = a[j];
        a[j] = x;
    }
    return a;
}