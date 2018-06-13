const assert = require('assert');

const sensor = require('./testdata/resources/sensor_testdata');
const artefact = require('./testdata/resources/artefact_testdata');
const ingestion = require('./testdata/resources/ingestion_testdata');
const sliceToConnectionArray = require('../src/interoperability/transform/slice_to_connection_array');
const data = require('./testdata/slice_to_connection_array_testdata');





describe('test data-generating-functions to build testslices', function() {
    it('test twoConnectedResources', function() {
        let slice = data.testdata_twoConnectedResources();
        let r1 = findResourceWithId("id_r1", slice);
        let r2 = findResourceWithId("id_r2", slice);

        let connections = sliceToConnectionArray.sliceToConnectionArray(slice);

        assert.equal(connections.length, 1);
        assert.equal(connections[0].source, r1);
        assert.equal(connections[0].target, r2);
        assert.deepEqual(connections[0].source.metadata.output, connections[0].target.metadata.input);
        testConnectionIsPresent(connections, r1, r2);
    });
    it('test twoIndependentResources', function() {
        let slice = data.testdata_twoIndependentResources();

        let connections = sliceToConnectionArray.sliceToConnectionArray(slice);

        assert.equal(connections.length, 0);
    });
    it('test circle_twoResources', function() {
        let slice = data.testdata_circle_twoResources();
        let r1 = findResourceWithId("id_r1", slice);
        let r2 = findResourceWithId("id_r2", slice);

        let connections = sliceToConnectionArray.sliceToConnectionArray(slice);

        assert.equal(connections.length, 2);
        testConnectionIsPresent(connections, r1, r2);
        testConnectionIsPresent(connections, r2, r1);
    });
    it('test diamond_fourResources', function() {
        let slice = data.testdata_diamond_fourResources();
        let r1 = findResourceWithId("id_r1", slice);
        let r2 = findResourceWithId("id_r2", slice);
        let r3 = findResourceWithId("id_r3", slice);
        let r4 = findResourceWithId("id_r4", slice);


        let connections = sliceToConnectionArray.sliceToConnectionArray(slice);

        assert.equal(connections.length, 4);
        testConnectionIsPresent(connections, r1, r2);
        testConnectionIsPresent(connections, r1, r3);
        testConnectionIsPresent(connections, r2, r4);
        testConnectionIsPresent(connections, r3, r4);
    });
    it('test diamondWithCircle_fourResources_oneUnconnectedResource', function() {
        let slice = data.testdata_diamondWithCircle_fourResources_oneUnconnectedResource();
        let r1 = findResourceWithId("id_r1", slice);
        let r2 = findResourceWithId("id_r2", slice);
        let r3 = findResourceWithId("id_r3", slice);
        let r4 = findResourceWithId("id_r4", slice);

        let connections = sliceToConnectionArray.sliceToConnectionArray(slice);

        assert.equal(connections.length, 6);
        //diamond
        testConnectionIsPresent(connections, r1, r2);
        testConnectionIsPresent(connections, r1, r3);
        testConnectionIsPresent(connections, r2, r4);
        testConnectionIsPresent(connections, r3, r4);

        //circle
        testConnectionIsPresent(connections, r2, r3);
        testConnectionIsPresent(connections, r3, r2);
    });
});


describe('test data-generating-functions to build testslices', function() {
    it('test #sliceConnectResources', function() {
        let r1 = data.emptyResource("id_r1");
        let r2 = data.emptyResource("id_r2");

        data.sliceConnectResources(r1, r2, {protocol:"mqtt"});

        assert.equal(r1.metadata.hasOwnProperty("outputs"), true);
        assert.equal(r2.metadata.hasOwnProperty("inputs"), true);
        assert.deepEqual(r1.metadata.output, r2.metadata.input);
    });
});


function testConnectionIsPresent(connections, source, target){
    assert.notEqual(connections.find(connectionBySourceAndDestination(source,target), undefined));
}

function connectionBySourceAndDestination(source, target){
    return function(connection){ return connection.source === source && connection.target === target};
}

function findResourceWithId(id, slice){
    let resourceArray = Object.keys(slice.resources).map(function (key) { return slice.resources[key]; });
    return resourceArray.find(resourceById(id));
}

function resourceById(id){
    return function(resource){ return resource.metadata.id === id;};
}