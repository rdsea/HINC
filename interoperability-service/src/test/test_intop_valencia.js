const assert = require('assert');

const util = require('../main/util/slice_util');
const check = require('../main/check/intop_check');
const recommendation = require('../main/recommendation/intop_recommendation');

const configModule = require('config');
let config = configModule.get('interoperability_service');



//TODO set solution resources
const valenciaTestResourcesDB = require('./testdata/valencia/db_resources/valencia_recommendation_resources');

let MongoClient = require("mongodb").MongoClient;


describe("valencia slices - intop check", function(){
    it("01_protocol",function(){
        const testslice = require('./testdata/valencia/slices/01_protocol');
        let result = check.checkSlice(testslice);
        assert.equal(result.errors.length, 4);
        assert.equal(result.matches.length, 6);
    });
    it("02_dataformat",function(){
        const testslice = require('./testdata/valencia/slices/02_dataformat');
        let result = check.checkSlice(testslice);
        assert.equal(result.errors.length, 3);
        assert.equal(result.matches.length, 7);
    });
    it("03_datacontract_jurisdiction",function(){
        const testslice = require('./testdata/valencia/slices/03_datacontract_jurisdiction');
        let result = check.checkSlice(testslice);
        assert.equal(result.errors.length, 1);
        assert.equal(result.matches.length, 1);
    });
    it("03contract_datacontract_jurisdiction",function(){
        const testslice = require('./testdata/valencia/slices/03_datacontract_jurisdiction');
        const contract = require('./testdata/valencia/slicecontracts/contract_03_jurisdiction');
        let result = check.checkWithContract(testslice,contract);
        assert.equal(result.contract_violations.length, 1);
        assert.equal(result.errors.length, 1);
        assert.equal(result.matches.length, 1);

    });
    it("04_datacontract_datarights",function(){
        const testslice = require('./testdata/valencia/slices/04_datacontract_datarights');
        const contract = require('./testdata/valencia/slicecontracts/contract_04_datarights');
        let result = check.checkWithContract(testslice,contract);
        assert.equal(result.contract_violations.length, 1);
        assert.equal(result.errors.length, 0);
        assert.equal(result.matches.length, 3);
    });
    it("05_datacontract_pricing",function(){
        const testslice = require('./testdata/valencia/slices/05_datacontract_pricing');
        const contract = require('./testdata/valencia/slicecontracts/contract_05_pricing');
        let result = check.checkWithContract(testslice,contract);
        assert.equal(result.errors.length, 0);
        assert.equal(result.contract_violations.length, 1);
        assert.equal(result.matches.length, 1);
    });
    it("06_qos_reliability",function(){
        const testslice = require('./testdata/valencia/slices/06_qos_reliability');
        const contract = require('./testdata/valencia/slicecontracts/contract_06_reliability');
        let result = check.checkWithContract(testslice,contract);
        assert.equal(result.errors.length, 0);
        assert.equal(result.contract_violations.length, 2);
        assert.equal(result.matches.length, 5);
    });
    it("07_qos_messagefrequency",function(){
        const testslice = require('./testdata/valencia/slices/07_qos_messagefrequency');
        let result = check.checkSlice(testslice);
        assert.equal(result.errors.length, 1);
        assert.equal(result.matches.length, 2);
    });
    it("07contract_qos_messagefrequency",function(){
        const testslice = require('./testdata/valencia/slices/07_qos_messagefrequency');
        const contract = require('./testdata/valencia/slicecontracts/contract_07_messagefrequency');
        let result = check.checkWithContract(testslice,contract);
        assert.equal(result.contract_violations.length, 1);
        assert.equal(result.errors.length, 1);
        assert.equal(result.matches.length, 2);
    });
    it("08_qod_precision",function(){
        const testslice = require('./testdata/valencia/slices/08_qod_precision');
        let result = check.checkSlice(testslice);
        assert.equal(result.errors.length, 1);
        assert.equal(result.matches.length, 2);
    });
    it("08contract_qod_precision",function(){
        const testslice = require('./testdata/valencia/slices/08_qod_precision');
        const contract = require('./testdata/valencia/slicecontracts/contract_08_precision');
        let result = check.checkWithContract(testslice,contract);
        assert.equal(result.contract_violations.length, 1);
        assert.equal(result.errors.length, 1);
        assert.equal(result.matches.length, 2);
    });
    it("09_qod_averagemeasurementage",function(){
        const testslice = require('./testdata/valencia/slices/09_qod_averagemeasurementage');
        let result = check.checkSlice(testslice);
        assert.equal(result.errors.length, 1);
        assert.equal(result.matches.length, 0);
    });
    it("09contract_qod_averagemeasurementage",function(){
        const testslice = require('./testdata/valencia/slices/09_qod_averagemeasurementage');
        const contract = require('./testdata/valencia/slicecontracts/contract_09_averagemeasurementage');
        let result = check.checkWithContract(testslice,contract);
        assert.equal(result.contract_violations.length, 1);
        assert.equal(result.errors.length, 1);
        assert.equal(result.matches.length, 0);
    });
});


describe("valencia slices - intop recommendation", function(){
    before(function() {
        return setUpTestDB(valenciaTestResourcesDB);
    });
    after(function() {
        return teardownTestDB();
    });

    it("01_protocol",function(){
        let slice = require('./testdata/valencia/slices/01_protocol');
        let old_slice = util.deepcopy(slice);
        //intopcheck returns no errors (and warnings)
        let checkresults = check.checkSlice(slice);
        let errors = checkresults.errors;
        assert.equal(errors.length, 4);
        //slice after recommendation equals before recommendation

        return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then(function(result) {
            let slice = result.slice;
            let logs = result.logs;

            assert.equal(util.contains(slice, "amqp_to_mqtt"), true);
            assert.equal(util.contains(slice, "mqtt_to_amqp"), true);
            assert.equal(util.contains(slice, "amqpbroker"), true);
            assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.intop_mqtt_to_amqp), true);
            assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_to_amqp, slice.resources.amqpbroker), true);
            assert.equal(util.isConnected(slice, slice.resources.amqpbroker, slice.resources.vessel), true);

            assert.equal(util.isConnected(slice, slice.resources.intop_amqp_to_mqtt, slice.resources.broker), true);
            assert.equal(util.isConnected(slice, slice.resources.amqpbroker_1, slice.resources.intop_amqp_to_mqtt), true);
            assert.equal(util.isConnected(slice, slice.resources.vessel, slice.resources.amqpbroker_1), true);


            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
    });
    it("02_dataformat",function(){
        let slice = require('./testdata/valencia/slices/02_dataformat');
        let old_slice = util.deepcopy(slice);
        //intopcheck returns no errors (and warnings)
        let checkresults = check.checkSlice(slice);
        let errors = checkresults.errors;
        assert.equal(errors.length, 3);
        //slice after recommendation equals before recommendation

        return recommendation.getRecommendationsWithoutCheck(slice, checkresults).then(function(result) {
            let slice = result.slice;
            let logs = result.logs;

            assert.equal(util.contains(slice, "mqtt_transformer_csv_to_json"), true);
            assert.equal(util.contains(slice, "mqtt_transformer_json_to_csv"), true);
            assert.equal(util.contains(slice, "mqttbroker"), true);
            assert.equal(util.isConnected(slice, slice.resources.broker, slice.resources.intop_mqtt_transformer_csv_to_json), true);
            assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_transformer_csv_to_json, slice.resources.mqttbroker_1), true);
            assert.equal(util.isConnected(slice, slice.resources.mqttbroker_1, slice.resources.pcs), true);

            assert.equal(util.isConnected(slice, slice.resources.pcs, slice.resources.mqttbroker), true);
            assert.equal(util.isConnected(slice, slice.resources.mqttbroker, slice.resources.intop_mqtt_transformer_json_to_csv), true);
            assert.equal(util.isConnected(slice, slice.resources.intop_mqtt_transformer_json_to_csv, slice.resources.broker), true);


            //intopcheck returns 0 error
            errors = check.checkSlice(slice).errors;
            assert.equal(errors.length, 0);
        });
    });
    xit("07_qos_messagefrequency",function(){
        let slice = require('./testdata/valencia/slices/07_qos_messagefrequency');
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
});

xdescribe("valencia slices - intop find substitution resources", function() {
    before(function () {
        return setUpTestDB(valenciaTestResourcesDB);
    });
    after(function () {
        return teardownTestDB();
    });

    xit("03_datacontract_jurisdiction",function(){
        let slice = require('./testdata/valencia/slices/03_datacontract_jurisdiction');
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
    xit("04_datacontract_datarights",function(){
        let slice = require('./testdata/valencia/slices/04_datacontract_datarights');
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
    xit("05_datacontract_pricing",function(){
        let slice = require('./testdata/valencia/slices/05_datacontract_pricing');
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
    xit("06_qos_reliability",function(){
        let slice = require('./testdata/valencia/slices/06_qos_reliability');
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
    xit("08_qod_precision",function(){
        let slice = require('./testdata/valencia/slices/08_qod_precision');
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
    xit("09_qod_averagemeasurementage",function(){
        let slice = require('./testdata/valencia/slices/09_qod_averagemeasurementage');
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
});


function setUpTestDB(testdata){
    MongoClient = require("mongodb").MongoClient;
    recommendation.queryServices = function (query) {
        return new Promise((resolve, reject) => {
            MongoClient.connect(config.MONGODB_URL, {useNewUrlParser: true}, function (err, db) {
                if (err) return reject(err);
                let dbo = db.db("recommendation_test");
                dbo.collection("test").find(query).toArray(function (err, result) {
                    if (err) throw err;
                    db.close();
                    result.forEach(e => {
                        e._type = "resource"
                    });
                    resolve(result);
                });
            });
        })
    };

    let promises = [];
    promises.push(new Promise(function (resolve, reject) {
        MongoClient.connect(config.MONGODB_URL, {useNewUrlParser: true}, function (err, db) {
            if (err) return reject(err);
            let dbo = db.db("recommendation_test");
            dbo.collection("test").insertMany(testdata, function (err, res) {
                if (err) throw err;
                console.log("Number of documents inserted: " + res.insertedCount);
                db.close();
                resolve();
            });
        });
    }));
    promises.push(new Promise(function (resolve, reject) {
        MongoClient.connect(config.MONGODB_URL, {useNewUrlParser: true}, function (err, db) {
            if (err) return reject(err);
            let dbo = db.db("recommendation_test");
            dbo.createCollection("recommendations", function (err, res) {
                if (err) throw err;
                db.close();
                resolve();
            });
        });
    }));
    return Promise.all(promises);
}

function teardownTestDB(){
    let promises = [];
    promises.push(new Promise(function (resolve, reject) {
        MongoClient.connect(config.MONGODB_URL, {useNewUrlParser: true}, function (err, db) {
            if (err) {
                db.close();
                return reject(err);
            }
            let dbo = db.db("recommendation_test");
            dbo.collection("test").drop(function (err, delOK) {
                db.close();
                if (err) return reject(err);
                if (delOK) console.log("Collection deleted");
                resolve();
            });
        });
    }));
    promises.push(new Promise(function (resolve, reject) {
        MongoClient.connect(config.MONGODB_URL, {useNewUrlParser: true}, function (err, db) {
            if (err) {
                db.close();
                return reject(err);
            }
            let dbo = db.db("recommendation_test");
            dbo.collection("recommendations").drop(function (err, delOK) {
                db.close();
                if (err) return reject(err);
                if (delOK) console.log("Collection deleted");
                resolve();
            });
        });
    }));
    return Promise.all(promises);
}

