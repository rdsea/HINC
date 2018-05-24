const sensor = require('../../../slice-management-client/test/testdata/resources/sensor_testdata');
const messagebroker = require('../../../slice-management-client/test/testdata/resources/broker_testdata');
const artefactrunner = require('../../../slice-management-client/test/testdata/resources/artefact_testdata');
const ingestion = require('../../../slice-management-client/test/testdata/resources/ingestion_testdata');


exports.graphTrue = function () {
    return true;
};

exports.graphFalse = function () {
    return false;
};


exports.test01 = function(){
    return [
      sensor.prototype,
      messagebroker.prototype,
      artefactrunner.prototype,
      ingestion.prototype
    ];
};


exports.demoSlice = function(){
    return [
        sensor.demo,
        messagebroker.demo,
        artefactrunner.demo,
        ingestion.demo
    ];
};




