const assert = require('assert');

const util = require('./util/slice_util');
const check = require('../src/interoperability/check/intop_check');
const recommendation = require('../src/interoperability/recommendation/intop_recommendation');
const basic_data = require('./testdata/testslices/basic_testslices');


describe('intop_recommendation', function(){
    describe('0 - basic testcases on minimalistic slices', function(){
        it('0_0_working: working slice, should not change', function () {
            let slice = basic_data.test_0_working_slice();
            let old_slice = util.deepcopy(slice);
            //intopcheck returns no errors (and warnings)
            let errors = check.check(slice).errors;
            assert.equal(errors.length, 0);
            //slice after recommendation equals before recommendation

            //TODO recommendation

            assert.deepEqual(slice, old_slice, "");

            //intopcheck returns 0 error
            errors = check.check(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_1_addition: direct mismatch, should add mediator', function(){
            let slice = basic_data.test_1_direct_mismatch();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let errors = check.check(slice).errors;
            assert.equal(errors.length, 1);
            //TODO recommendation

            /* recommendation:
                - +1 resource (broker)
                - no connection between source and dest
                - connection between: source->broker, broker->dest
             */
            let count = util.resourceCount(slice);
            assert.equal(count, old_count+1);
            assert.equal(util.contains(slice, broker), true);
            assert.equal(util.isConnected(slice, source, dest), false);
            assert.equal(util.isConnected(slice, source, broker), true);
            assert.equal(util.isConnected(slice, broker, dest), true);

            //intopcheck returns 0 error
            errors = check.check(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_2_addition: indirect mismatch, should add mediator', function(){
            let slice = basic_data.test_2_indirect_mismatch();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let errors = check.check(slice).errors;
            assert.equal(errors.length, 1);
            //TODO recommendation

            /* recommendation - Solution A (and new broker between sensor and transformer):
                - +2 resource (broker, transformer)
                - no connection between source and original broker
                - connection between: source->newbroker, newbroker->transformer, transformer->orig.broker
             */
            let countA = util.resourceCount(slice);
            assert.equal(countA, old_count+2);
            assert.equal(util.contains(slice, newbroker), true);
            assert.equal(util.contains(slice, transformer), true);
            assert.equal(util.isConnected(slice, source, origbroker), false);
            assert.equal(util.isConnected(slice, source, newbroker), true);
            assert.equal(util.isConnected(slice, newbroker, transformer), true);
            assert.equal(util.isConnected(slice, transformer, origbroker), true);
            assert.equal(util.isConnected(slice, origbroker, dest), true);

            // Alternative solution <-- not implemented
            /* recommendation - Solution B:
                - +1 resource (transformer)
                - source.output.topic != dest.input.topic
                - connection between: transformer->broker, broker->transformer
                - transformer.input.topic = source.output.topic
                - transformer.output.topic = dest.input.topic
             */

            //intopcheck returns 0 error
            errors = check.check(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_3_substitution: wrong broker, should substitute broker', function () {
            let slice = basic_data.test_3_substitution();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns >=1 error
            let errors = check.check(slice).errors;
            assert.equal(errors.length>=1, true);
            //TODO recommendation

            /* recommendation:
                - resourcecount equal
                - broker.protocol == mqtt
                - connection between: source->broker, broker->dest
             */
            let count = util.resourceCount(slice);
            assert.equal(count, old_count);
            assert.equal(util.contains(slice, oldbroker), false);
            assert.equal(util.contains(slice, newbroker), true);
            assert.equal(util.isConnected(slice, source, newbroker), true);
            assert.equal(util.isConnected(slice, newbroker, dest), true);
            //TODO assert brokerprotocol

            //intopcheck returns 0 error
            errors = check.check(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_4_reduction: broker not needed, should remove broker', function () {
            let slice = basic_data.test_4_reduction();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error

            /* recommendation:
                - -1 resource (broker)
                - connection between: source->dest
             */
            let count = util.resourceCount(slice);
            assert.equal(count, old_count-1);
            assert.equal(util.contains(slice, broker), false);
            assert.equal(util.isConnected(slice, source, dest), true);

            //intopcheck returns 0 error
            errors = check.check(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('0_5_addition: multiple push-pull problem with needed broker, should add poller and buffer', function () {
            let slice = basic_data.test_5_push_pull();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let errors = check.check(slice).errors;
            assert.equal(errors.length, 1);
            //TODO recommendation

            /* recommendation:
                - +2 resources (poller, buffer)
                - no connection between: source->broker, broker->http_dest
                - connection between: source->poller, poller->broker, broker->buffer, buffer->http_dest
             */
            let count = util.resourceCount(slice);
            assert.equal(count, old_count+2);

            assert.equal(util.contains(slice, poller), true);
            assert.equal(util.contains(slice, buffer), true);

            assert.equal(util.isConnected(slice, source, broker), false);
            assert.equal(util.isConnected(slice, broker, http_dest), false);

            assert.equal(util.isConnected(slice, source, poller), true);
            assert.equal(util.isConnected(slice, poller, broker), true);
            assert.equal(util.isConnected(slice, broker, buffer), true);
            assert.equal(util.isConnected(slice, buffer, http_dest), true);
            assert.equal(util.isConnected(slice, broker, mqtt_dest), true);

            //intopcheck returns 0 error
            errors = check.check(slice).errors;
            assert.equal(errors.length, 0);
        });
    });

    describe('1 - bts scenario (actual testslices)', function(){
        it('1_0_testslice0: working slice, should not change', function(){
            let slice = {};
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns no errors (and warnings)
            let checkresult = check.check(slice);
            assert.equal(checkresult.errors.length, 0);
            assert.equal(checkresult.warnings.length, 0);
            //TODO recommendation
            //slice after recommendation equals before recommendation
            assert.deepEqual(slice, old_slice);

            //intopcheck returns 0 error
            errors = check.check(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('1_1_testslice1: csv to json, should add resources', function(){
            let slice = {};
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let errors = check.check(slice).errors;
            assert.equal(errors.length, 1);
            //TODO recommendation


            /* recommendation - Solution A (and new broker between sensor and transformer):
                - +2 resource (broker, transformer)
                - no connection between sensor and original broker
                - connection between: sensor->newbroker, newbroker->transformer, transformer->orig.broker
             */
            let countA = util.resourceCount(slice);
            assert.equal(countA, old_count+2);
            assert.equal(util.contains(slice, newbroker), true);
            assert.equal(util.contains(slice, transformer), true);
            assert.equal(util.isConnected(slice, csv_sensor, origbroker), false);
            assert.equal(util.isConnected(slice, csv_sensor, newbroker), true);
            assert.equal(util.isConnected(slice, newbroker, transformer), true);
            assert.equal(util.isConnected(slice, transformer, origbroker), true);
            assert.equal(util.isConnected(slice, origbroker, dest), true);


            // Alternative solution <-- not implemented
            /* recommendation - Solution B:
                - +1 resource (transformer)
                - sensor.output.topic != ingestion.input.topic
                - connection between: transformer->broker, broker->transformer
                - transformer.input.topic = sensor.output.topic
                - transformer.output.topic = ingestion.input.topic
             */

            //intopcheck returns 0 error
            errors = check.check(slice).errors;
            assert.equal(errors.length, 0);
        });
        it('1_2_testslice2: csv to json with multiple sensors, should add resources', function(){
            let slice = {};
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let errors = check.check(slice).errors;
            assert.equal(errors.length, 1);
            //TODO recommendation


            /* recommendation - Solution A (and new broker between sensor and transformer):
                - +2 resource (broker, transformer)
                - no connection between sensor and original broker
                - connection between: sensor1->newbroker, newbroker->transformer, transformer->orig.broker
             */
            let countA = util.resourceCount(slice);
            assert.equal(countA, old_count+2);
            assert.equal(util.contains(slice, newbroker), true);
            assert.equal(util.contains(slice, transformer), true);
            assert.equal(util.isConnected(slice, csv_sensor, origbroker), false);
            assert.equal(util.isConnected(slice, csv_sensor, newbroker), true);
            assert.equal(util.isConnected(slice, newbroker, transformer), true);
            assert.equal(util.isConnected(slice, transformer, origbroker), true);
            assert.equal(util.isConnected(slice, json_sensor, origbroker), true);
            assert.equal(util.isConnected(slice, origbroker, dest), true);


            // Alternative solution <-- not implemented
            /* recommendation - Solution B:
                - +1 resource (transformer)
                - sensor1.output.topic != ingestion.input.topic
                - connection between: transformer->broker, broker->transformer
                - transformer.input.topic = sensor1.output.topic
                - transformer.output.topic = ingestion.input.topic
             */

            //intopcheck returns 0 error
            errors = check.check(slice).errors;
            assert.equal(errors.length, 0);
        });
    });
});
