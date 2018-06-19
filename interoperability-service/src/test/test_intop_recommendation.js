const assert = require('assert');

const util = require('../main/util/slice_util');
const check = require('../main/check/intop_check');
const recommendation = require('../main/recommendation/intop_recommendation');

const solutionResources = require('./testdata/intop_recommendation_testdata');

const basic_data = require('./testdata/testslices/basic_testslices');

const bts_testslice_0 = require('./testdata/testslices/bts_testslice0');
const bts_testslice_1 = require('./testdata/testslices/bts_testslice1');
const bts_testslice_2 = require('./testdata/testslices/bts_testslice2');


describe('intop_recommendation', function(){
    describe('0 - basic testcases on minimalistic slices', function(){
        it('0_0_working: working slice, should not change', function () {
            let slice = basic_data.test_0_working_slice();
            let old_slice = util.deepcopy(slice);
            //intopcheck returns no errors (and warnings)
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 0);
            //slice after recommendation equals before recommendation

            let resources = solutionResources.solutionResources_test_0_0();
            recommendation.setTestMode(true,resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);

            assert.deepEqual(slice, old_slice, "");

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_1_addition: direct dataformat mismatch, should add mediator', function(){
            let slice = basic_data.test_1_direct_mismatch();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            let resources = solutionResources.solutionResources_test_0_1();
            recommendation.setTestMode(true, resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);

            /* recommendation:
                - +1 resource (broker)
                - no connection between source and dest
                - connection between: source->broker, broker->dest
             */
            let count = util.resourceCount(slice);
            assert.equal(count, old_count+1);
            assert.equal(util.contains(slice, "transformer"), true);
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.dest), false);
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.intop_transformer), true);
            assert.equal(util.isConnected(slice, slice.resources.intop_transformer, slice.resources.dest), true);

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_2_addition: indirect mismatch, should add mediator', function(){
            let slice = basic_data.test_2_indirect_mismatch();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            let resources = solutionResources.solutionResources_test_0_2();
            recommendation.setTestMode(true, resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);

            /* recommendation - Solution A (and new broker between sensor and transformer):
                - +2 resource (broker, transformer)
                - no connection between source and original broker
                - connection between: source->newbroker, newbroker->transformer, transformer->orig.broker
             */
            let countA = util.resourceCount(slice);
            assert.equal(countA, old_count+2);
            assert.equal(util.contains(slice, "newbroker"), true);
            assert.equal(util.contains(slice, "transformer"), true);
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.broker), false);
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.intop_newbroker), true);
            assert.equal(util.isConnected(slice, slice.resources.intop_newbroker, slice.resources.intop_transformer), true);
            assert.equal(util.isConnected(slice, slice.resources.intop_transformer, slice.resources.broker), true);
            assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.dest), true);

            // Alternative solution <-- not implemented
            /* recommendation - Solution B:
                - +1 resource (transformer)
                - source.output.topic != dest.input.topic
                - connection between: transformer->broker, broker->transformer
                - transformer.input.topic = source.output.topic
                - transformer.output.topic = dest.input.topic
             */

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_3_substitution: wrong broker, should substitute broker', function () {
            let slice = basic_data.test_3_substitution();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns >=1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length>=1, true);

            let resources = solutionResources.solutionResources_test_0_3();
            recommendation.setTestMode(true, resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);

            /* recommendation:
                - resourcecount equal
                - broker.protocol == mqtt
                - connection between: source->broker, broker->dest
             */
            let count = util.resourceCount(slice);
            assert.equal(count, old_count);
            assert.equal(util.contains(slice, "oldbroker"), false);
            assert.equal(util.contains(slice, "newbroker"), true);
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.newbroker), true);
            assert.equal(util.isConnected(slice, slice.resources.newbroker, slice.resources.dest), true);
            //TODO assert brokerprotocol

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_4_reduction: broker not needed, should remove broker', function () {
            let slice = basic_data.test_4_reduction();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 2 errors
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 2);

            let resources = solutionResources.solutionResources_test_0_4();
            recommendation.setTestMode(true, resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);

            /* recommendation:
                - -1 resource (broker)
                - connection between: source->dest
             */
            let count = util.resourceCount(slice);
            assert.equal(count, old_count-1);
            assert.equal(util.contains(slice, "broker"), false);
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.dest), true);

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_5_addition: multiple push-pull problem with needed broker, should add poller and buffer', function () {
            let slice = basic_data.test_5_push_pull();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 2 errors
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 2);

            let resources = solutionResources.solutionResources_test_0_5();
            recommendation.setTestMode(true, resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);

            /* recommendation:
                - +2 resources (poller, buffer)
                - no connection between: source->broker, broker->http_dest
                - connection between: source->poller, poller->broker, broker->buffer, buffer->http_dest
             */
            let count = util.resourceCount(slice);
            assert.equal(count, old_count+2);

            assert.equal(util.contains(slice, "poller"), true);
            assert.equal(util.contains(slice, "buffer"), true);

            //TODO
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.broker), false);
            assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.http_dest), false);

            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.poller), true);
            assert.equal(util.isConnected(slice, slice.resources.poller, slice.resources.broker), true);
            assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.buffer), true);
            assert.equal(util.isConnected(slice, slice.resources.buffer, slice.resources.http_dest), true);
            assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.mqtt_dest), true);

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_6_addition: missing message_broker, should add broker', function(){
            //TODO check test
            let slice = basic_data.test_6_missing_broker();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            let resources = solutionResources.solutionResources_test_0_6();
            recommendation.setTestMode(true, resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);

            /* recommendation:
                - +1 resource (broker)
                - no connection between source and dest
                - connection between: source->broker, broker->dest
             */
            let count = util.resourceCount(slice);
            assert.equal(count, old_count+1);
            assert.equal(util.contains(slice, "broker"), true);
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.dest), false);
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.intop_broker), true);
            assert.equal(util.isConnected(slice, slice.resources.intop_broker, slice.resources.dest), true);

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_7_addition: missing broker & direct dataformat mismatch, should add broker + mediator', function(){
            //TODO check test
            let slice = basic_data.test_7_missing_broker_and_dataformat_mismatch();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            let resources = solutionResources.solutionResources_test_0_7();
            recommendation.setTestMode(true, resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);

            /* recommendation:
                - +1 resource (broker)
                - no connection between source and dest
                - connection between: source->broker, broker->dest
             */
            let count = util.resourceCount(slice);
            assert.equal(count, old_count+1);
            assert.equal(util.contains(slice, "transformer"), true);
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.dest), false);
            assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.intop_transformer), true);
            assert.equal(util.isConnected(slice, slice.resources.intop_transformer, slice.resources.dest), true);

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
    });

    describe('1 - bts scenario (actual testslices)', function(){
        it('1_0_testslice0: working slice, should not change', function(){
            let slice = bts_testslice_0;
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns no errors (and warnings)
            let checkresults = check.checkSlice(slice);
            assert.equal(checkresults.errors.length, 0);
            assert.equal(checkresults.warnings.length, 0);

            let resources = solutionResources.solutionResources_test_1_0();
            recommendation.setTestMode(true, resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);
            //slice after recommendation equals before recommendation
            assert.deepEqual(slice, old_slice);

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('1_1_testslice1: csv to json, should add resources', function(){
            let slice = bts_testslice_1;
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            let resources = solutionResources.solutionResources_test_1_1();
            recommendation.setTestMode(true, resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);


            /* recommendation - Solution A (and new broker between sensor and transformer):
                - +2 resource (broker, transformer)
                - no connection between sensor and original broker
                - connection between: sensor->newbroker, newbroker->transformer, transformer->orig.broker
             */
            let countA = util.resourceCount(slice);
            assert.equal(countA, old_count+2);
            assert.equal(util.contains(slice, "newbroker"), true);
            assert.equal(util.contains(slice, "transformer"), true);
            assert.equal(util.isConnected(slice, slice.resources.csv_sensor, slice.resources.origbroker), false);
            assert.equal(util.isConnected(slice, slice.resources.csv_sensor, slice.resources.newbroker), true);
            assert.equal(util.isConnected(slice, slice.resources.newbroker, slice.resources.transformer), true);
            assert.equal(util.isConnected(slice, slice.resources.transformer, slice.resources.origbroker), true);
            assert.equal(util.isConnected(slice, slice.resources.origbroker, slice.resources.dest), true);


            // Alternative solution <-- not implemented
            /* recommendation - Solution B:
                - +1 resource (transformer)
                - sensor.output.topic != ingestion.input.topic
                - connection between: transformer->broker, broker->transformer
                - transformer.input.topic = sensor.output.topic
                - transformer.output.topic = ingestion.input.topic
             */

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('1_2_testslice2: csv to json with multiple sensors, should add resources', function(){
            let slice = bts_testslice_2;
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            let resources = solutionResources.solutionResources_test_1_2();
            recommendation.setTestMode(true, resources);
            slice = recommendation.applyRecommendationsWithoutCheck(old_slice, checkresults);


            /* recommendation - Solution A (and new broker between sensor and transformer):
                - +2 resource (broker, transformer)
                - no connection between sensor and original broker
                - connection between: sensor1->newbroker, newbroker->transformer, transformer->orig.broker
             */
            let countA = util.resourceCount(slice);
            assert.equal(countA, old_count+2);
            assert.equal(util.contains(slice, "newbroker"), true);
            assert.equal(util.contains(slice, "transformer"), true);
            assert.equal(util.isConnected(slice, slice.resources.csv_sensor, slice.resources.origbroker), false);
            assert.equal(util.isConnected(slice, slice.resources.csv_sensor, slice.resources.newbroker), true);
            assert.equal(util.isConnected(slice, slice.resources.newbroker, slice.resources.transformer), true);
            assert.equal(util.isConnected(slice, slice.resources.transformer, slice.resources.origbroker), true);
            assert.equal(util.isConnected(slice, slice.resources.json_sensor, slice.resources.origbroker), true);
            assert.equal(util.isConnected(slice, slice.resources.origbroker, slice.resources.dest), true);


            // Alternative solution <-- not implemented
            /* recommendation - Solution B:
                - +1 resource (transformer)
                - sensor1.output.topic != ingestion.input.topic
                - connection between: transformer->broker, broker->transformer
                - transformer.input.topic = sensor1.output.topic
                - transformer.output.topic = ingestion.input.topic
             */

            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
    });
});
