const assert = require('assert');

const util = require('../main/util/slice_util');
const testslice = require('./testSlice');
const basic_slices = require('./testdata/testslices/basic_testslices');

describe('test slice_util', function() {
    it('test slice_util#resourceCount', function() {
        let count = util.resourceCount(testslice);
        assert.equal(count, 4);
    });
    it('test slice_util#isConnected should return true', function() {
        let source = testslice.resources.sensor;
        let destination = testslice.resources.broker;
        let isConnected = util.isConnected(testslice, source, destination);
        assert.equal(isConnected, true);
    });
    it('test slice_util#isConnected should return false', function() {
        let source = testslice.resources.broker;
        let destination = testslice.resources.sensor;
        let isConnected = util.isConnected(testslice, source, destination);
        assert.equal(isConnected, false);
    });
    it('test slice_util#contains should return true', function() {
        let resource = testslice.resources.ingest.name;
        let contains = util.contains(testslice, resource);
        assert.equal(contains, true);
    });
    it('test slice_util#contains should return false', function() {
        let resource = "new resource not in testslice";
        let contains = util.contains(testslice, resource);
        assert.equal(contains, false);
    });
    it('test slice_util#sliceConnect should connect resources in slice', function() {
        let source = {name:"source", source:[], target:[]};
        let dest = {name:"dest", source:[], target:[]};

        let slice = {resources:{sourceId:source, dest:dest}, connectivities:{}};
        let isConnected = util.isConnected(slice,source,dest);
        assert.equal(isConnected, false);

        util.sliceConnect(slice,source,dest,"connectionname");
        isConnected = util.isConnected(slice,source,dest);
        assert.equal(isConnected, true);

        let wrongDirectionConnected = util.isConnected(slice,dest,source);
        assert.equal(wrongDirectionConnected, false);

    });
    it('test slice_util#sliceDisConnect should connect resources in slice', function() {
        let source = {name:"source", source:[], target:[]};
        let dest = {name:"dest", source:[], target:[]};

        let slice = {resources:{source:source, dest:dest}, connectivities:{}};
        let isConnected = util.isConnected(slice,source,dest);
        assert.equal(isConnected, false);

        util.sliceConnect(slice,source,dest,"connectionname");
        isConnected = util.isConnected(slice,source,dest);
        assert.equal(isConnected, true);

        let wrongDirectionConnected = util.isConnected(slice,dest,source);
        assert.equal(wrongDirectionConnected, false);


        util.sliceDisConnect(slice,source,dest);
        isConnected = util.isConnected(slice,source,dest);
        assert.equal(isConnected, false);
        assert.equal(slice.connectivities["connectionname"], undefined);
        assert.equal(slice.resources.source.target.indexOf("connectionname")>-1, false);
        assert.equal(slice.resources.dest.source.indexOf("connectionname")>-1, false);
    });
    it('test slice_util#sliceAddResource should add resources with new name', function() {
        let source = {name: "source", source: [], target: []};
        let dest = {name: "dest", source: [], target: []};

        let slice = {resources: {source: source, dest: dest}, connectivities: {}};

        let extraSource = {name: "extraSource", source: [], target: []};

        let resourceId = util.sliceAddResource(slice, extraSource, "source");
        assert.equal(resourceId, "source_1");
        assert.equal(slice.resources["source_1"], extraSource);
    });
    it('test slice_util#substituteResource should correctly substitute resources', function() {
        let slice = basic_slices.test_0_10_indirect_mismatch_mn();
        let oldbroker = util.deepcopy(slice.resources.broker);

        let substitutionBroker = {name: "substitutionBroker", metadata:{test:"test"}, source: [], target: []};

        util.substituteResource(slice, "broker", substitutionBroker);

        //let resourceId = util.sliceAddResource(slice, extraSource, "source");
        assert.notDeepEqual(slice.resources.broker, oldbroker, "oldbroker should not be part of the slice anymore");
        assert.equal(slice.resources.broker.name, substitutionBroker.name);
        assert.deepEqual(slice.resources.broker.metadata, substitutionBroker.metadata);
        assert.equal(util.isConnected(slice,slice.resources.source1,slice.resources.broker), true);
        assert.equal(util.isConnected(slice,slice.resources.source2,slice.resources.broker), true);
        assert.equal(util.isConnected(slice,slice.resources.broker, slice.resources.dest1), true);
        assert.equal(util.isConnected(slice,slice.resources.broker, slice.resources.dest2), true);
    });

});
