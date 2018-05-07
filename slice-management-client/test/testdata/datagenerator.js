const sensor = require('./resources/sensor_testdata');
const messagebroker = require('./resources/broker_testdata');
const artefactrunner = require('./resources/artefact_testdata');
const ingestion = require('./resources/ingestion_testdata');


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




