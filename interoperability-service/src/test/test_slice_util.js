const assert = require('assert');

const util = require('../main/util/slice_util');
const testslice = require('./testSlice');

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
        //TODO fix testslice
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

});
