const intop_check = require('../src/interoperability/check/intop_check');
const testdata = require('./testdata/intop_check_testdata');
const assert = require('assert');



/*let connection = testdata.no_input();
            let result = intop_check.checkSingleConnection(connection);

            assert.equal(result.errors.length, 1);
            assert.equal(result.warnings.length, 0);
            assert.equal(result.matches.length, 0);
*/
describe('intop_recommendation', function(){
    describe('0 - basic testcases on minimalistic slices', function(){
        it('0_0_working: working slice, should not change', function () {
            //intopcheck returns no errors (and warnings)
            //slice after recommendation equals before recommendation

            //intopcheck returns 0 error
        });
        it('0_1_addition: direct mismatch, should add mediator', function(){
            //intopcheck returns 1 error

            /* recommendation:
                - +1 resource (broker)
                - no connection between source and dest
                - connection between: source->broker, broker->dest
             */

            //intopcheck returns 0 error
        });
        it('0_2_addition: indirect mismatch, should add mediator', function(){
            //intopcheck returns 1 error

            /* recommendation - Solution A (and new broker between sensor and transformer):
                - +2 resource (broker, transformer)
                - no connection between source and original broker
                - connection between: source->newbroker, newbroker->transformer, transformer->orig.broker
             */

            //intopcheck returns 0 error
            /* recommendation - Solution B:
                - +1 resource (transformer)
                - source.output.topic != dest.input.topic
                - connection between: transformer->broker, broker->transformer
                - transformer.input.topic = source.output.topic
                - transformer.output.topic = dest.input.topic
             */

            //intopcheck returns 0 error
        });
        it('0_3_substitution: wrong broker, should substitute broker', function () {
            //intopcheck returns >=1 error

            /* recommendation:
                - resourcecount equal
                - broker.protocol == mqtt
                - connection between: source->broker, broker->dest
             */

            //intopcheck returns 0 error
        });
        it('0_4_reduction: broker not needed, should remove broker', function () {
            //intopcheck returns 1 error

            /* recommendation:
                - -1 resource (broker)
                - connection between: source->dest
             */

            //intopcheck returns 0 error
        });
        it('0_5_addition: multiple push-pull problem with needed broker, should add poller and server', function () {
            //intopcheck returns 1 error

            /* recommendation:
                - +2 resources (poller, server)
                - no connection between: source->broker, broker->http_dest
                - connection between: source->poller, poller->broker, broker->server, server,http_dest
             */
            //intopcheck returns 0 error
        });
    });

    describe('1 - bts scenario (actual testslices)', function(){
        it('1_0_testslice0: working slice, should not change', function(){
            //intopcheck returns no errors (and warnings)
            //slice after recommendation equals before recommendation

            //intopcheck returns 0 error
        });
        it('1_1_testslice1: csv to json, should add resources', function(){
            //intopcheck returns 1 error

            /* recommendation - Solution A (and new broker between sensor and transformer):
                - +2 resource (broker, transformer)
                - no connection between sensor and original broker
                - connection between: sensor->newbroker, newbroker->transformer, transformer->orig.broker
             */

            //intopcheck returns 0 error
            /* recommendation - Solution B:
                - +1 resource (transformer)
                - sensor.output.topic != ingestion.input.topic
                - connection between: transformer->broker, broker->transformer
                - transformer.input.topic = sensor.output.topic
                - transformer.output.topic = ingestion.input.topic
             */

            //intopcheck returns 0 error
        });
        it('1_2_testslice2: csv to json with multiple sensors, should add resources', function(){
            //intopcheck returns 1 error

            /* recommendation - Solution A (and new broker between sensor and transformer):
                - +2 resource (broker, transformer)
                - no connection between sensor and original broker
                - connection between: sensor1->newbroker, newbroker->transformer, transformer->orig.broker
             */

            //intopcheck returns 0 error
            /* recommendation - Solution B:
                - +1 resource (transformer)
                - sensor1.output.topic != ingestion.input.topic
                - connection between: transformer->broker, broker->transformer
                - transformer.input.topic = sensor1.output.topic
                - transformer.output.topic = ingestion.input.topic
             */

            //intopcheck returns 0 error
        });
    });
});
