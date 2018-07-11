const assert = require('assert');

const util = require('../main/util/slice_util');
const check = require('../main/check/intop_check');
const recommendation = require('../main/recommendation/intop_recommendation');
let config = require("../config");

const basic_data = require('./testdata/testslices/basic_testslices');

const bts_testslice_0 = require('./testdata/testslices/bts_testslice0');
const bts_testslice_1 = require('./testdata/testslices/bts_testslice1');
const bts_testslice_2 = require('./testdata/testslices/bts_testslice2');

const solutionResourcesDB = require('./testdata/testslices/intop_recommendation_db_dump');

const MongoClient = require("mongodb").MongoClient;

let test_mongodb_url = "mongodb://localhost:27017/";
let old_recommendation_history = config.MONGODB_URL;


describe('intop_recommendation', function(){
    before(function() {
        config.MONGODB_URL = test_mongodb_url;
        recommendation.queryServices = function(query){
            return new Promise((resolve, reject) => {
            MongoClient.connect(test_mongodb_url, function(err, db) {
                if (err) return reject(err);
                let dbo = db.db("recommendation_test");
                dbo.collection("test").find(query).toArray(function(err, result) {
                    if (err) throw err;
                    db.close();
                    resolve(result);
                });
            });
            })
        };

        return new Promise(function(resolve, reject){
            MongoClient.connect(test_mongodb_url, function(err, db) {
                if (err) return reject(err);
                let dbo = db.db("recommendation_test");
                dbo.collection("test").insertMany(solutionResourcesDB, function(err, res) {
                    if (err) throw err;
                    console.log("Number of documents inserted: " + res.insertedCount);
                    db.close();
                    resolve();
                });
            });
        });
    });
    after(function() {
        let promises = [];
        promises.push(new Promise(function(resolve, reject){
            MongoClient.connect(test_mongodb_url, function(err, db) {
                if (err) {db.close(); return reject(err);}
                let dbo = db.db("recommendation_test");
                dbo.collection("test").drop(function(err, delOK) {
                    db.close();
                    if (err) return reject(err);
                    if (delOK) console.log("Collection deleted");
                    resolve();
                });
            });
        }));
        promises.push(new Promise(function(resolve, reject){
            MongoClient.connect(test_mongodb_url, function(err, db) {
                if (err) {db.close(); return reject(err);}
                let dbo = db.db("recommendation_history");
                dbo.collection("recommendations").drop(function(err, delOK) {
                    db.close();
                    if (err) return reject(err);
                    if (delOK) console.log("Collection deleted");
                    resolve();
                });
            });
        }));
        config.MONGODB_URL = old_recommendation_history;
        return Promise.all(promises);
    });

    describe('0 - basic interoperability testcases on minimalistic slices', function(){
        it('0_0_working: working slice, should not change', function () {
            let slice = basic_data.test_0_0_working_slice();
            let old_slice = util.deepcopy(slice);
            //intopcheck returns no errors (and warnings)
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 0);
            //slice after recommendation equals before recommendation

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then(function(result) {
                let slice = result.slice;
                let logs = result.logs;

                assert.deepEqual(slice, old_slice);

                //intopcheck returns 0 error
                errors = check.checkSlice(slice).errors;
                assert.equal(errors.length, 0);
            });
        });
        it('0_1_addition: direct dataformat mismatch, should add mediator', function(){
            let slice = basic_data.test_0_1_direct_mismatch();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;

                /* recommendation:
                    - +1 resource (broker)
                    - no connection between source and dest
                    - connection between: source->broker, broker->dest
                 */
                let count = util.resourceCount(slice);
                assert.equal(count, old_count + 1);
                assert.equal(util.contains(slice, "http_transformer_csv_to_json"), true);
                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.dest), false);
                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.intop_http_transformer_csv_to_json), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_http_transformer_csv_to_json, slice.resources.dest), true);

                //intopcheck returns 0 error
                errors = check.checkSlice(slice).errors;
                assert.equal(errors.length, 0);
            });
        });
        it('0_2_addition: indirect mismatch, should add mediator', function(){
            let slice = basic_data.test_0_2_indirect_mismatch();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;


                /* recommendation - Solution A (and new broker between sensor and transformer):
                    - +2 resource (broker, transformer)
                    - no connection between source and original broker
                    - connection between: source->newbroker, newbroker->transformer, transformer->orig.broker
                 */
                let countA = util.resourceCount(slice);
                assert.equal(countA, old_count+2);
                assert.equal(util.contains(slice, "mqttbroker"), true);
                assert.equal(util.contains(slice, "mqtt_transformer_csv_to_json"), true);
                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.broker), false);
                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.mqttbroker), true);
                assert.equal(util.isConnected(slice, slice.resources.mqttbroker, slice.resources.intop_mqtt_transformer_csv_to_json), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_transformer_csv_to_json, slice.resources.broker), true);
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
        });
        //TODO implement algorithm to pass test and activate it again (remove x from xit)
        xit('0_3_substitution: wrong broker, should substitute broker', function () {
            let slice = basic_data.test_0_3_substitution();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns >=1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length>=1, true);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;

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
        });
        //TODO implement algorithm to pass test and activate it again (remove x from xit)
        xit('0_4_reduction: broker not needed, should remove broker', function () {
            let slice = basic_data.test_0_4_reduction();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 2 errors
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 2);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;

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
        });
        it('0_5_addition: multiple push-pull problem with needed broker, should add poller and buffer', function () {
            let slice = basic_data.test_0_5_push_pull();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 2 errors
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 2);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;

                /* recommendation:
                    - +2 resources (poller, buffer)
                    - no connection between: source->broker, broker->http_dest
                    - connection between: source->poller, poller->broker, broker->buffer, buffer->http_dest
                 */
                let count = util.resourceCount(slice);
                assert.equal(count, old_count+2);

                assert.equal(util.contains(slice, "http_mqtt_poller"), true);
                assert.equal(util.contains(slice, "mqtt_http_buffer"), true);

                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.broker), false);
                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.httpdest), false);

                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.intop_http_mqtt_poller), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_http_mqtt_poller, slice.resources.broker), true);
                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.intop_mqtt_http_buffer), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_http_buffer, slice.resources.httpdest), true);
                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.mqttdest), true);

                //intopcheck returns 0 error
                errors = check.checkSlice(slice).errors;
                assert.equal(errors.length, 0);
            });
        });
        it('0_6_addition: missing message_broker, should add broker', function(){
            let slice = basic_data.test_0_6_missing_broker();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;

                /* recommendation:
                    - +1 resource (broker)
                    - no connection between source and dest
                    - connection between: source->broker, broker->dest
                 */
                let count = util.resourceCount(slice);
                assert.equal(count, old_count+1);
                assert.equal(util.contains(slice, "mqttbroker"), true);
                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.dest), false);
                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.intop_mqttbroker), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_mqttbroker, slice.resources.dest), true);

                //intopcheck returns 0 error
                errors = check.checkSlice(slice).errors;
                assert.equal(errors.length, 0);
            });
        });
        //TODO implement algorithm to pass test and activate it again (remove x from xit)
        xit('0_7_addition: missing broker & direct dataformat mismatch, should add broker + transformer', function(){
            let slice = basic_data.test_0_7_missing_broker_and_dataformat_mismatch();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;

                /* recommendation:
                    - +3 resource (broker, transformer, broker)
                    - no connection between source and dest
                    - connection between: source->broker1->transformer->broker2->dest
                 */
                let count = util.resourceCount(slice);
                assert.equal(count, old_count+3);
                assert.equal(util.contains(slice, "transformer"), true);
                assert.equal(util.contains(slice, "broker1"), true);
                assert.equal(util.contains(slice, "broker2"), true);
                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.dest), false);

                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.intop_broker), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_broker, slice.resources.intop_transformer), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_transformer, slice.resources.intop_broker_1), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_broker_1, slice.resources.dest), true);

                //intopcheck returns 0 error
                errors = check.checkSlice(slice).errors;
                assert.equal(errors.length, 0);
            });
        });
        it('0_8_addition: indirect mismatch M:1, should add transformer at problematic source', function(){
            let slice = basic_data.test_0_8_indirect_mismatch_m1();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;

                /* recommendation - Solution A (and new broker between sensor and transformer):
                    - +2 resource (broker, transformer)
                    - no connection between source and original broker
                    - connection between: source->newbroker, newbroker->transformer, transformer->orig.broker
                 */
                let countA = util.resourceCount(slice);
                assert.equal(countA, old_count+2);
                assert.equal(util.contains(slice, "mqttbroker"), true);
                assert.equal(util.contains(slice, "mqtt_transformer_csv_to_json"), true);
                assert.equal(util.isConnected(slice, slice.resources.source2, slice.resources.broker), false);

                assert.equal(util.isConnected(slice, slice.resources.source1, slice.resources.broker), true);
                assert.equal(util.isConnected(slice, slice.resources.source2, slice.resources.mqttbroker), true);
                assert.equal(util.isConnected(slice, slice.resources.mqttbroker, slice.resources.intop_mqtt_transformer_csv_to_json), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_transformer_csv_to_json, slice.resources.broker), true);
                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.dest), true);

                //intopcheck returns 0 error
                errors = check.checkSlice(slice).errors;
                assert.equal(errors.length, 0);
            });
        });
        it('0_9_addition: indirect mismatch 1:N, should add transformer at problematic dest', function(){
            let slice = basic_data.test_0_9_indirect_mismatch_1n();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;

                /* recommendation - Solution A (and new broker between sensor and transformer):
                    - +2 resource (broker, transformer)
                    - no connection between source and original broker
                    - connection between: source->newbroker, newbroker->transformer, transformer->orig.broker
                 */
                let countA = util.resourceCount(slice);
                assert.equal(countA, old_count+2);
                assert.equal(util.contains(slice, "mqttbroker"), true);
                assert.equal(util.contains(slice, "mqtt_transformer_json_to_csv"), true);
                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.dest2), false);

                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.broker), true);

                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.dest1), true);
                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.intop_mqtt_transformer_json_to_csv), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_transformer_json_to_csv, slice.resources.mqttbroker), true);
                assert.equal(util.isConnected(slice, slice.resources.mqttbroker, slice.resources.dest2), true);

                //intopcheck returns 0 error
                errors = check.checkSlice(slice).errors;
                assert.equal(errors.length, 0);
            });
        });
        it('0_10_addition: indirect mismatch M:N, should add transformers for only one dataformat', function(){
            let slice = basic_data.test_0_10_indirect_mismatch_mn();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 2);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;

                /* recommendation - Solution A (and new broker between sensor and transformer):
                    - +2 resource (broker, transformer)
                    - no connection between source and original broker
                    - connection between: source->newbroker, newbroker->transformer, transformer->orig.broker
                 */
                let countA = util.resourceCount(slice);
                assert.equal(countA, old_count+4);
                assert.equal(util.contains(slice, "mqttbroker"), true);
                assert.equal(util.contains(slice, "mqtt_transformer_csv_to_json"), true);
                assert.equal(util.contains(slice, "mqtt_transformer_json_to_csv"), true);

                //JSON is selected as dataformat
                assert.equal(util.isConnected(slice, slice.resources.source2, slice.resources.broker), false);
                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.dest2), false);

                assert.equal(util.isConnected(slice, slice.resources.source1, slice.resources.broker), true);
                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.dest1), true);

                assert.equal(util.isConnected(slice, slice.resources.source2, slice.resources.mqttbroker_1), true);
                assert.equal(util.isConnected(slice, slice.resources.mqttbroker_1, slice.resources.intop_mqtt_transformer_csv_to_json), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_transformer_csv_to_json, slice.resources.broker), true);

                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.intop_mqtt_transformer_json_to_csv), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_transformer_json_to_csv, slice.resources.mqttbroker), true);
                assert.equal(util.isConnected(slice, slice.resources.mqttbroker, slice.resources.dest2), true);

                //intopcheck returns 0 error
                errors = check.checkSlice(slice).errors;
                assert.equal(errors.length, 0);
            });
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

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then(function(result) {
                let slice = result.slice;
                let logs = result.logs;
                assert.equal(util.resourceCount(slice), old_count);

                //slice after recommendation equals before recommendation
                //TODO: add metadata for brokerInOutPuts only temporarily
                //assert.deepEqual(slice, old_slice);

                //intopcheck returns 0 error
                errors = check.checkSlice(slice).errors;
                assert.equal(errors.length, 0);
            });
        });
        it('1_1_testslice1: csv to json, should add resources', function(){
            let slice = bts_testslice_1;
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then(function(result) {
                let slice = result.slice;
                let logs = result.logs;



                /* recommendation - Solution A (and new broker between sensor and transformer):
                    - +2 resource (broker, transformer)
                    - no connection between sensor and original broker
                    - connection between: sensor->newbroker, newbroker->transformer, transformer->orig.broker
                 */
                let countA = util.resourceCount(slice);
                assert.equal(countA, old_count+2);
                assert.equal(util.contains(slice, "mqttbroker"), true);
                assert.equal(util.contains(slice, "mqtt_transformer_csv_to_json"), true);
                assert.equal(util.isConnected(slice, slice.resources.csv_sensor, slice.resources.broker), false);
                assert.equal(util.isConnected(slice, slice.resources.csv_sensor, slice.resources.mqttbroker), true);
                assert.equal(util.isConnected(slice, slice.resources.mqttbroker, slice.resources.intop_mqtt_transformer_csv_to_json), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_transformer_csv_to_json, slice.resources.broker), true);
                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.ingest), true);
                assert.equal(util.isConnected(slice, slice.resources.ingest, slice.resources.bigquery), true);

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
        });
        it('1_2_testslice2: csv to json with multiple sensors, should add resources', function(){
            let slice = bts_testslice_2;
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);
            //intopcheck returns 1 error
            let checkresults = check.checkSlice(slice);
            let errors = checkresults.errors;
            assert.equal(errors.length, 1);

            return recommendation.getRecommendationsWithoutCheck(old_slice, checkresults).then(function(result) {
                let slice = result.slice;
                let logs = result.logs;


                /* recommendation - Solution A (and new broker between sensor and transformer):
                    - +2 resource (broker, transformer)
                    - no connection between sensor and original broker
                    - connection between: sensor1->newbroker, newbroker->transformer, transformer->orig.broker
                 */
                let countA = util.resourceCount(slice);
                assert.equal(countA, old_count+2);
                assert.equal(util.contains(slice, "mqttbroker"), true);
                assert.equal(util.contains(slice, "mqtt_transformer_csv_to_json"), true);
                assert.equal(util.isConnected(slice, slice.resources.csv_sensor, slice.resources.broker), false);
                assert.equal(util.isConnected(slice, slice.resources.csv_sensor, slice.resources.mqttbroker), true);
                assert.equal(util.isConnected(slice, slice.resources.mqttbroker, slice.resources.intop_mqtt_transformer_csv_to_json), true);
                assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_transformer_csv_to_json, slice.resources.broker), true);
                assert.equal(util.isConnected(slice, slice.resources.json_sensor, slice.resources.broker), true);
                assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.ingest), true);
                assert.equal(util.isConnected(slice, slice.resources.ingest, slice.resources.bigquery), true);

                assert.deepEqual(slice.resources.broker.metadata, old_slice.resources.broker.metadata);

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



    describe('2 - basic datacontract testcases with minimalistic slices and datacontracts', function(){
        it('2_0_reliability: reliability of source too low', function(){
            let slice = basic_data.test_2_0_datacontract_reliability();
            let old_slice = util.deepcopy(slice);
            let old_count = util.resourceCount(old_slice);

            let contract = {qos:{reliability:99.9}};
            let result = check.checkWithContract(slice, contract);

            assert.equal(result.errors.length, 0);
            assert.equal(result.matches.length, 1);
            assert.equal(result.contract_violations.length, 2);


            return recommendation.getContractRecommendationsWithoutCheck(slice, contract, result).then( function(result) {
                let slice = result.slice;
                let logs = result.logs;

                let count = util.resourceCount(slice);
                assert.equal(count, old_count);
                assert.equal(util.isConnected(slice, slice.resources.source, slice.resources.dest), true);
                assert.equal(util.contains(slice, "reliable_httpsource"), true);
                assert.equal(slice.resources.source.name, "reliable_httpsource");
                assert.equal(slice.resources.source.metadata.resource.qos.reliability>=contract.qos.reliability, true);

                assert.equal(util.contains(slice, "reliable_httpdest"), true);
                assert.equal(slice.resources.dest.name, "reliable_httpdest");
                assert.equal(slice.resources.dest.metadata.resource.qos.reliability>=contract.qos.reliability, true);

                //intopcheck returns 0 error
                let checkresults = check.checkWithContract(slice, contract);
                assert.equal(checkresults.errors.length, 0);
                assert.equal(checkresults.contract_violations.length, 0);
            });

        });
    })
});
