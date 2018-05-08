const intop_check = require('../src/interoperability/check/intop_check');
const testdata = require('./testdata/intop_check_testdata');
const assert = require('assert');


describe('intop_check.check', function(){
    /*let slice = datagenerator.demoSlice();
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
        let slice = data.testdata_diamond_fourResources();
        //let slice = datagenerator.test01();
        let problems = intop_check.check(slice);

        assert.equal(problems.length , 0);
    });*/

    describe('single output/input', function(){
        it('matching output/input, should not return error or warning', function(){
            let connection = testdata.single_inout_matching();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 0);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 1);
        });

        it('mismatching protocol, should return error', function(){
            let connection = testdata.single_inout_mismatching_protocol();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 1);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 0);
        });

        it('mismatching protocol.qos, should return warning', function(){
            let connection = testdata.single_inout_mismatching_protocolQos();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 0);
            assert.equal(result.warnings.length, 1);
        });
    });

    describe('multiple output/input', function(){
        it('one matching output/input, should not create error or warning', function(){
            let connection = testdata.multiple_inout_matching();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 0);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 1);
        });

        it('all mismatching protocol, should create error', function(){
            let connection = testdata.multiple_inout_mismatching_protocol();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 1);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 0);
        });

        it('one mismatching protocol.qos, multiple mismatching protocol, should create warning', function(){
            let connection = testdata.multiple_inout_mismatching_protocolQos();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 0);
            assert.equal(result.warnings.length, 1);
        });

        it('one matching output/input, one mismatching protocol.qos, should not create error or warning', function(){
            let connection = testdata.multiple_inout_matching_one_mismatching_protocolQos();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 0);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 1);
        });
    });

    describe('output/broker', function(){
        it('matching output/broker, should not create error or warning', function(){
            let connection = testdata.broker_output_matching();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 0);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 1);
        });

        it('mismatching protocol, should create error', function(){
            let connection = testdata.broker_output_mismatching_protocol();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 1);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 0);
        });
    });

    describe('broker/input', function(){
        it('matching broker/input, should not create error or warning', function(){
            let connection = testdata.broker_input_matching();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 0);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 1);
        });

        it('mismatching protocol, should create error', function(){
            let connection = testdata.broker_input_mismatching_protocol();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 1);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 0);
        });
    });

    describe('no input/output', function(){
        it('no output, should create error', function(){
            let connection = testdata.no_input();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 1);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 0);
        });
        it('no input, should create error', function(){
            let connection = testdata.no_output();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 1);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 0);
        });
    });
});



describe('basic checks', function(){
    describe('protocol check', function () {
        
    });
    describe('dataformat check', function () {

    });
    describe('qod check', function () {

    });
    describe('qos check', function () {

    });
});