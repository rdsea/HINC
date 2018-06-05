const intop_check = require('../src/interoperability/check/intop_check');
const assert = require('assert');

const testdata = require('./testdata/intop_check_testdata');

const basic_testslices = require('./testdata/testslices/basic_testslices');

const bts_testslice_0 = require('./testdata/testslices/bts_testslice0');
const bts_testslice_1 = require('./testdata/testslices/bts_testslice1');
const bts_testslice_2 = require('./testdata/testslices/bts_testslice2');


describe('intop_check.check', function(){
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


describe('intop_check.checkSlice', function(){
    describe('0 - basic testcases on minimalistic slices', function(){
        it('0_0_working: working slice, should not change', function () {
            let slice = basic_testslices.test_0_working_slice();
            intop_check.checkSlice(slice);
        });
        it('0_1_addition: direct mismatch, should add mediator', function(){
            let slice = basic_testslices.test_1_direct_mismatch();
            intop_check.checkSlice(slice);
        });
        it('0_2_addition: indirect mismatch, should add mediator', function(){
            let slice = basic_testslices.test_2_indirect_mismatch();
            intop_check.checkSlice(slice);
        });
        it('0_3_substitution: wrong broker, should substitute broker', function () {
            let slice = basic_testslices.test_3_substitution();
            intop_check.checkSlice(slice);
        });
        it('0_4_reduction: broker not needed, should remove broker', function () {
            let slice = basic_testslices.test_4_reduction();
            intop_check.checkSlice(slice);
        });
        it('0_5_addition: multiple push-pull problem with needed broker, should add poller and buffer', function () {
            let slice = basic_testslices.test_5_push_pull();
            intop_check.checkSlice(slice);
        });
    });

    describe('1 - bts scenario (actual testslices)', function(){
        it('1_0_testslice0: working slice, should not change', function(){
            //TODO correct slice --> correct connections
            /*let slice = bts_testslice_0;
            intop_check.checkSlice(slice);*/
        });
        it('1_1_testslice1: csv to json, should add resources', function(){
            //TODO correct slice --> correct connections
            /*let slice = bts_testslice_1;
            intop_check.checkSlice(slice);*/
        });
        it('1_2_testslice2: csv to json with multiple sensors, should add resources', function(){
            //TODO correct slice --> correct connections
            /*let slice = bts_testslice_2;
            intop_check.checkSlice(slice);*/
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