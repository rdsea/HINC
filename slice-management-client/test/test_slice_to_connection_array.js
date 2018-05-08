const assert = require('assert');

const datagenerator = require('./testdata/datagenerator');
const sensor = require('./testdata/resources/sensor_testdata');
const artefact = require('./testdata/resources/artefact_testdata');
const ingestion = require('./testdata/resources/ingestion_testdata');
const sliceToConnectionArray = require('../src/interoperability/transform/slice_to_connection_array');
const data = require('./testdata/slice_to_connection_array_testdata');





describe('test data-generating-functions to build testslices', function() {
    it('test twoConnectedResources', function() {
        let slice = data.testdata_twoConnectedResources();
        let r1 = slice.resources.find(resourceById("r1"));
        let r2 = slice.resources.find(resourceById("r2"));

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
        let r1 = slice.resources.find(resourceById("r1"));
        let r2 = slice.resources.find(resourceById("r2"));

        let connections = sliceToConnectionArray.sliceToConnectionArray(slice);

        assert.equal(connections.length, 2);
        testConnectionIsPresent(connections, r1, r2);
        testConnectionIsPresent(connections, r2, r1);
    });
    it('test diamond_fourResources', function() {
        let slice = data.testdata_diamond_fourResources();
        let r1 = slice.resources.find(resourceById("r1"));
        let r2 = slice.resources.find(resourceById("r2"));
        let r3 = slice.resources.find(resourceById("r3"));
        let r4 = slice.resources.find(resourceById("r4"));


        let connections = sliceToConnectionArray.sliceToConnectionArray(slice);

        assert.equal(connections.length, 4);
        testConnectionIsPresent(connections, r1, r2);
        testConnectionIsPresent(connections, r1, r3);
        testConnectionIsPresent(connections, r2, r4);
        testConnectionIsPresent(connections, r3, r4);
    });
    it('test diamond_fourResources_shuffled', function() {
        let slice = data.testdata_diamond_fourResources_shuffled();
        let r1 = slice.resources.find(resourceById("r1"));
        let r2 = slice.resources.find(resourceById("r2"));
        let r3 = slice.resources.find(resourceById("r3"));
        let r4 = slice.resources.find(resourceById("r4"));

        let connections = sliceToConnectionArray.sliceToConnectionArray(slice);

        assert.equal(connections.length, 4);
        testConnectionIsPresent(connections, r1, r2);
        testConnectionIsPresent(connections, r1, r3);
        testConnectionIsPresent(connections, r2, r4);
        testConnectionIsPresent(connections, r3, r4);
    });
    it('test diamondWithCircle_fourResources_oneUnconnectedResource', function() {
        let slice = data.testdata_diamondWithCircle_fourResources_oneUnconnectedResource();
        let r1 = slice.resources.find(resourceById("r1"));
        let r2 = slice.resources.find(resourceById("r2"));
        let r3 = slice.resources.find(resourceById("r3"));
        let r4 = slice.resources.find(resourceById("r4"));

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
        let r1 = data.emptyResource("r1");
        let r2 = data.emptyResource("r2");

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

function resourceById(id){
    return function(resource){ return resource.metadata.id === id;};
}