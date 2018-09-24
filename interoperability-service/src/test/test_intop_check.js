const intop_check = require('../main/check/intop_check');
const assert = require('assert');
const util = require('../main/util/slice_util');

const testdata = require('./testdata/intop_check_testdata');

const basic_testslices = require('./testdata/testslices/basic_testslices');

const bts_testslice_0 = require('./testdata/testslices/bts_testslice0');
const bts_testslice_1 = require('./testdata/testslices/bts_testslice1');
const bts_testslice_2 = require('./testdata/testslices/bts_testslice2');



describe('intop_check.checkSlice', function(){
    describe('0 - basic interoperability testcases on minimalistic slices', function(){
        it('0_0_working: working slice, should not change', function () {
            let slice = basic_testslices.test_0_0_working_slice();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 0);
            assert.equal(result.matches.length, 1);
        });
        it('0_1_addition: direct mismatch, should add mediator', function(){
            let slice = basic_testslices.test_0_1_direct_mismatch();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 1);
            assert.equal(result.matches.length, 0);
        });
        it('0_2_addition: indirect mismatch, should add mediator', function(){
            let slice = basic_testslices.test_0_2_indirect_mismatch();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 1);
            assert.equal(result.matches.length, 2);
        });
        it('0_3_substitution: wrong broker, should substitute broker', function () {
            let slice = basic_testslices.test_0_3_substitution();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 2);
            assert.equal(result.matches.length, 1);
        });
        it('0_4_reduction: broker not needed, should remove broker', function () {
            let slice = basic_testslices.test_0_4_reduction();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 2);
            assert.equal(result.matches.length, 1);
        });
        it('0_5_addition: multiple push-pull problem with needed broker, should add poller and buffer', function () {
            let slice = basic_testslices.test_0_5_push_pull();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 2);
            assert.equal(result.matches.length, 3);
        });
        it('0_6_addition: missing message_broker, should add broker', function(){
            let slice = basic_testslices.test_0_6_missing_broker();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 1);
            assert.equal(result.matches.length, 0);
        });
        it('0_7_addition: missing broker & direct dataformat mismatch, should add broker + transformer', function(){
            let slice = basic_testslices.test_0_7_missing_broker_and_dataformat_mismatch();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 1);
            assert.equal(result.matches.length, 0);
        });
        it('0_8_addition: indirect mismatch M:1, should add transformer at problematic source', function(){
            let slice = basic_testslices.test_0_8_indirect_mismatch_m1();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 1);
            assert.equal(result.matches.length, 4);
        });
        it('0_9_addition: indirect mismatch 1:N, should add transformer at problematic dest', function(){
            let slice = basic_testslices.test_0_9_indirect_mismatch_1n();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 1);
            assert.equal(result.matches.length, 4);
        });
        it('0_10_addition: indirect mismatch M:N, should add transformers for only one dataformat', function(){
            let slice = basic_testslices.test_0_10_indirect_mismatch_mn();
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 2);
            assert.equal(result.matches.length, 6);
        });
    });

    describe('1 - bts scenario (actual testslices)', function(){
        it('1_0_testslice0: working slice, should not change', function(){
            let slice = bts_testslice_0;
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 0);
            assert.equal(result.matches.length, 4);
        });
        it('1_1_testslice1: csv to json, should add resources', function(){
            let slice = bts_testslice_1;
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 1);
            assert.equal(result.matches.length, 3);
        });
        it('1_2_testslice2: csv to json with multiple sensors, should add resources', function(){
            let slice = bts_testslice_2;
            intop_check.checkSlice(slice);
            let result = intop_check.checkSlice(slice);
            assert.equal(result.errors.length, 1);
            assert.equal(result.matches.length, 5)
        });
    });

    describe('2 - basic datacontract testcases with minimalistic slices and datacontracts', function(){
        it('2_0_reliability: reliability of source too low', function(){
            let slice = basic_testslices.test_2_0_datacontract_reliability();
            let contract = {qos:{reliability:98}};
            let result = intop_check.checkWithContract(slice, contract);
            assert.equal(result.errors.length, 0);
            assert.equal(result.matches.length, 1);
            assert.equal(result.contract_violations.length, 2);

            contract.qos.reliability = 90;
            result = intop_check.checkWithContract(slice, contract);
            assert.equal(result.contract_violations.length, 1);
            contract.qos.reliability = 89;
            result = intop_check.checkWithContract(slice, contract);
            assert.equal(result.contract_violations.length, 0);


            result = intop_check.checkWithContract(slice, {});
            assert.equal(result.contract_violations.length, 0);
        });
    })
});

describe('basic checks', function(){
    it('checkslice.addBrokerInoutPuts doesnt persist changes to the metadata of broker', function () {
        let slice = basic_testslices.test_0_2_indirect_mismatch();
        let old_slice = util.deepcopy(slice);
        let result = intop_check.checkSlice(slice);

        assert.deepEqual(slice.resources.broker.metadata, old_slice.resources.broker.metadata);

        slice = basic_testslices.test_0_2_indirect_mismatch();
        slice.resources.broker.metadata.inputs = [];
        slice.resources.broker.metadata.outputs = [];
        old_slice = util.deepcopy(slice);
        result = intop_check.checkSlice(slice);

        assert.deepEqual(slice.resources.broker.metadata, old_slice.resources.broker.metadata);
    });
    describe('protocol check', function () {
        
    });
    describe('dataformat check', function () {

    });
    describe('qod check', function () {

    });
    describe('qos check', function () {

    });
});