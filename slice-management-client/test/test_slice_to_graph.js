const assert = require('assert');

const datagenerator = require('./testdata/datagenerator');
const sensor = require('./testdata/sensor_testdata');
const artefact = require('./testdata/artefact_testdata');
const ingestion = require('./testdata/ingestion_testdata');
const sliceToGraph = require('../src/interoperability/slice_to_graph');
const data = require('./testdata/slice_to_graph_testdata');





describe('test data-generating-functions to build testslices', function() {
    it('test twoConnectedResources', function() {
        let slice = data.testdata_twoConnectedResources();
        let r1 = slice.find(resourceById("r1"));
        let r2 = slice.find(resourceById("r2"));

        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(graph.edges.length, 1);
        assert.equal(graph.edges[0].source, r1);
        assert.equal(graph.edges[0].destination, r2);
        assert.deepEqual(graph.edges[0].source.metadata.output, graph.edges[0].destination.metadata.input);
        testEdgeIsPresent(graph, r1, r2);
    });
    it('test twoIndependentResources', function() {
        let slice = data.testdata_twoIndependentResources();

        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(graph.edges.length, 0);
    });
    it('test circle_twoResources', function() {
        let slice = data.testdata_circle_twoResources();
        let r1 = slice.find(resourceById("r1"));
        let r2 = slice.find(resourceById("r2"));

        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(graph.edges.length, 2);
        testEdgeIsPresent(graph, r1, r2);
        testEdgeIsPresent(graph, r2, r1);
    });
    it('test diamond_fourResources', function() {
        let slice = data.testdata_diamond_fourResources();
        let r1 = slice.find(resourceById("r1"));
        let r2 = slice.find(resourceById("r2"));
        let r3 = slice.find(resourceById("r3"));
        let r4 = slice.find(resourceById("r4"));


        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(graph.edges.length, 4);
        testEdgeIsPresent(graph, r1, r2);
        testEdgeIsPresent(graph, r1, r3);
        testEdgeIsPresent(graph, r2, r4);
        testEdgeIsPresent(graph, r3, r4);
    });
    it('test diamond_fourResources_shuffled', function() {
        let slice = data.testdata_diamond_fourResources_shuffled();
        let r1 = slice.find(resourceById("r1"));
        let r2 = slice.find(resourceById("r2"));
        let r3 = slice.find(resourceById("r3"));
        let r4 = slice.find(resourceById("r4"));

        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(graph.edges.length, 4);
        testEdgeIsPresent(graph, r1, r2);
        testEdgeIsPresent(graph, r1, r3);
        testEdgeIsPresent(graph, r2, r4);
        testEdgeIsPresent(graph, r3, r4);
    });
    it('test diamondWithCircle_fourResources_oneUnconnectedResource', function() {
        let slice = data.testdata_diamondWithCircle_fourResources_oneUnconnectedResource();
        let r1 = slice.find(resourceById("r1"));
        let r2 = slice.find(resourceById("r2"));
        let r3 = slice.find(resourceById("r3"));
        let r4 = slice.find(resourceById("r4"));

        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(graph.edges.length, 6);
        //diamond
        testEdgeIsPresent(graph, r1, r2);
        testEdgeIsPresent(graph, r1, r3);
        testEdgeIsPresent(graph, r2, r4);
        testEdgeIsPresent(graph, r3, r4);

        //circle
        testEdgeIsPresent(graph, r2, r3);
        testEdgeIsPresent(graph, r3, r2);
    });
});


describe('test data-generating-functions to build testslices', function() {
    it('test #sliceConnectResources', function() {
        let r1 = data.emptyResource("r1");
        let r2 = data.emptyResource("r2");

        data.sliceConnectResources(r1, r2, {protocol:"mqtt"});

        assert.equal(r1.metadata.hasOwnProperty("output"), true);
        assert.equal(r2.metadata.hasOwnProperty("input"), true);
        assert.deepEqual(r1.metadata.output, r2.metadata.input);
    });
});

describe('test resource filters', function() {
    let hasNoInputs = sensor.prototype;
    let hasBoth = artefact.prototype;
    let hasNoOutputs = ingestion.prototype;
    let emptyInOut = {metadata:{input:[], output:[]}};
    let inOut = {metadata:{input:[1], output:[1]}};

    let testslice = [hasNoInputs, hasBoth, hasNoOutputs, emptyInOut, inOut];

    it('should return ingestion & emptyInOut', function() {

        let result = testslice.filter(sliceToGraph.hasNoOutputs);
        assert.equal(result.length, 2);
        assert.equal(result[0].metadata.category, "ingestion");
    });

    it('should return only sensor & emptyInOut', function() {

        let result = testslice.filter(sliceToGraph.hasNoInputs);
        assert.equal(result.length, 2);
        assert.equal(result[0].metadata.category, "sensor");
    });

    it('should return only artefact & inOut', function() {

        let result = testslice.filter(sliceToGraph.hasBoth);
        assert.equal(result.length, 2);
        assert.equal(result[0].metadata.category, "datatransformer");
    });
});

function testEdgeIsPresent(graph, source, destination){
    assert.notEqual(graph.edges.find(edgeBySourceAndDestination(source,destination), undefined));
}

function edgeBySourceAndDestination(source, destination){
    return function(edge){ return edge.source === source && edge.destination === destination};
}

function resourceById(id){
    return function(resource){ return resource.metadata.id === id;};
}