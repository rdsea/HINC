const assert = require('assert');

const sensor = require('./testdata/resources/sensor_testdata');
const artefact = require('./testdata/resources/artefact_testdata');
const ingestion = require('./testdata/resources/ingestion_testdata');
const sliceToGraph = require('../src/interoperability/transform/slice_to_graph');
const data = require('./testdata/slice_to_connection_array_testdata');





describe('test data-generating-functions to build testslices', function() {
    it('test twoConnectedResources', function() {
        let slice = data.testdata_twoConnectedResources();

        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(Object.keys(graph.nodes).length, 2);
        assert.equal(Object.keys(graph.edges).length, 1);
        assert.equal(graph.startNodes.length, 1);

        let r1node = graph.nodes.r1;
        let nextnodes = sliceToGraph.nextNodes(graph, r1node);
        let nextnextnodes = sliceToGraph.nextNodes(graph, nextnodes[0]);

        assert.equal(nextnodes.length , 1);
        assert.equal(nextnextnodes.length , 0);
    });
    it('test twoIndependentResources', function() {
        let slice = data.testdata_twoIndependentResources();
        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(Object.keys(graph.nodes).length, 2);
        assert.equal(Object.keys(graph.edges).length, 0);
        assert.equal(graph.startNodes.length, 0);

        let r1node = graph.nodes.r1;
        let nextnodes = sliceToGraph.nextNodes(graph, r1node);

        assert.equal(nextnodes.length , 0);
    });
    it('test circle_twoResources', function() {
        let slice = data.testdata_circle_twoResources();
        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(Object.keys(graph.nodes).length, 2);
        assert.equal(Object.keys(graph.edges).length, 2);
        assert.equal(graph.startNodes.length, 0);

        let r1node = graph.nodes.r1;
        let nextnodes = sliceToGraph.nextNodes(graph, r1node);
        let nextnextnodes = sliceToGraph.nextNodes(graph, nextnodes[0]);
        let lastnodes = sliceToGraph.nextNodes(graph, nextnextnodes[0]);

        assert.equal(nextnodes.length , 1);
        assert.equal(nextnextnodes.length , 1);
        assert.equal(lastnodes.length , 1);
    });
    it('test diamond_fourResources', function() {
        let slice = data.testdata_diamond_fourResources();

        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(Object.keys(graph.nodes).length, 4);
        assert.equal(Object.keys(graph.edges).length, 4);
        assert.equal(graph.startNodes.length, 1);

        let r1node = graph.nodes.r1;
        let nextnodes = sliceToGraph.nextNodes(graph, r1node);
        let nextnextnodes = sliceToGraph.nextNodes(graph, nextnodes[0]);
        let lastnodes = sliceToGraph.nextNodes(graph, nextnextnodes[0]);

        assert.equal(nextnodes.length , 2);
        assert.equal(nextnextnodes.length , 1);
        assert.equal(lastnodes.length , 0);

    });
    it('test diamondWithCircle_fourResources_oneUnconnectedResource', function() {
        let slice = data.testdata_diamondWithCircle_fourResources_oneUnconnectedResource();

        let graph = sliceToGraph.sliceToGraph(slice);

        assert.equal(Object.keys(graph.nodes).length, 5);
        assert.equal(Object.keys(graph.edges).length, 6);
        assert.equal(graph.startNodes.length, 1);

        let r1node = graph.nodes.r1;
        let nextnodes = sliceToGraph.nextNodes(graph, r1node);
        let nextnextnodes = sliceToGraph.nextNodes(graph, nextnodes[0]);
        let lastnodes = sliceToGraph.nextNodes(graph, nextnextnodes[0]);

        assert.equal(nextnodes.length , 2);
        assert.equal(nextnextnodes.length , 2);
        assert.equal(lastnodes.length , 0);
    });
});


describe('test data-generating-functions to build testslices', function() {
    it('test #sliceConnectResources', function() {
        let r1 = data.emptyResource("id_r1");
        let r2 = data.emptyResource("id_r2");
        let slice = {resources:{"r1":r1,"r2":r2}, connectivities:{}};

        data.sliceConnectResources(slice, r1, r2, {protocol:"mqtt"});

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