const assert = require('assert');

const util = require('./slice_util');
const testslice = require('../testSlice');

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
        let resource = testslice.resources.ingest;
        let contains = util.contains(testslice, resource);
        assert.equal(contains, true);
    });
    it('test slice_util#contains should return false', function() {
        let resource = {name:"new resource not in testslice"};
        let contains = util.contains(testslice, resource);
        assert.equal(contains, false);
    });
    it('test slice_util#sliceConnect should connect resources in slice', function() {
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

    });
});
