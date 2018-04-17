const intop_check = require('../src/interoperability/intop_check');
const datagenerator = require('./testdata/datagenerator');
const assert = require('assert');


describe('intop_check.check', function(){
    let slice = datagenerator.demoSlice();
    let sensor = slice[0].metadata;
    let broker = slice[1].metadata;
    let artefact = slice[2].metadata;
    let ingestion = slice[3].metadata;

    it('test object', function () {


        console.info(broker)
        assert.equal(sensor.category,"sensor");
        assert.equal(broker.category,"messagebroker");
        assert.equal(artefact.category,"datatransformer");
        assert.equal(ingestion.category,"ingestion");
    });
    it('compareFunction1', function () {
        let slice = datagenerator.test01();
        assert.equal(intop_check.check(slice,null), true);
    });
    it('compareFunction2', function () {
        let slice = datagenerator.test01();
        assert.equal(intop_check.check(slice,intop_check.falseCompare),false)
    });
})