const sensor = require('./sensor_testdata');
const messagebroker = require('./broker_testdata');
const artefactrunner = require('./artefact_testdata');
const ingestion = require('./ingestion_testdata');


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




