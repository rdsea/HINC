const assert = require('assert');

const util = require('../main/util/slice_util');
const check = require('../main/check/intop_check');
const recommendation = require('../main/recommendation/intop_recommendation');

const configModule = require('config');
let config = configModule.get('interoperability_service');


describe("lingfan evaluation slices - intop check", function(){
    it("01_slice_PM_tests",function(){
        const testslice = require('../../client_testslices/lingfan_evaluation/slice_PM_tests');
        let result = check.checkSlice(testslice);
        assert.equal(result.errors.length, 0);
        assert.equal(result.matches.length, 6);
    });
    it("02_slice_UC01_add_data_consumer",function(){
        const testslice = require('../../client_testslices/lingfan_evaluation/slice_UC01_add_data_consumer');
        let result = check.checkSlice(testslice);
        assert.equal(result.errors.length, 0);
        assert.equal(result.matches.length, 2);
    });
    it("03_slice_UC03_protect_data_consumer",function(){
        const testslice = require('../../client_testslices/lingfan_evaluation/slice_UC03_protect_data_consumer');
        let result = check.checkSlice(testslice);
        assert.equal(result.errors.length, 0);
        assert.equal(result.matches.length, 5);
    });
    it("04_slice_UC04_custom_analysis_logic",function(){
        const testslice = require('../../client_testslices/lingfan_evaluation/slice_UC04_custom_analysis_logic');
        let result = check.checkSlice(testslice);
        assert.equal(result.errors.length, 0);
        assert.equal(result.matches.length, 6);
    });
});


