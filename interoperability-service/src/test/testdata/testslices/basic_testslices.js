const util = require('../../../main/util/slice_util');
const builder = require("../testslice_builder_util");

exports.test_0_0_working_slice = function(){
    let slice = builder.empty_slice("test_0_0_working_slice");
    slice.resources["source"] = builder.source("source", "push", builder.httpProtocol(), builder.jsonFormat());
    slice.resources["dest"] = builder.dest("dest", "push", builder.httpProtocol(), builder.jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest, "connectionId1");
    return slice;
};
exports.test_0_1_direct_mismatch = function(){
    let slice = builder.empty_slice("test_0_1_direct_mismatch");
    slice.resources["source"] = builder.source("source", "push", builder.httpProtocol(), builder.csvFormat());
    slice.resources["dest"] = builder.dest("dest", "push", builder.httpProtocol(), builder.jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest, "connectionId1");
    return slice;
};
exports.test_0_2_indirect_mismatch = function(){
    let slice = builder.empty_slice("test_0_2_indirect_mismatch");
    slice.resources["source"] = builder.source("source", "push", builder.mqttProtocol(), builder.csvFormat());
    slice.resources["dest"] = builder.dest("dest", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["broker"] = builder.mqttBroker("broker",builder.mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest, "connectionId2");
    return slice;
};
exports.test_0_3_substitution = function(){
    let slice = builder.empty_slice("test_0_3_substitution");
    slice.resources["source"] = builder.source("source", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["dest"] = builder.dest("dest", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["oldbroker"] = builder.amqpBroker("oldbroker",builder.amqpProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.oldbroker, "connectionId1");
    util.sliceConnect(slice, slice.resources.oldbroker, slice.resources.dest, "connectionId2");
    return slice;
};
exports.test_0_4_reduction = function(){
    let slice = builder.empty_slice("test_0_4_reduction");
    slice.resources["source"] = builder.source("source", "pull", builder.httpProtocol(), builder.jsonFormat());
    slice.resources["dest"] = builder.dest("dest", "pull", builder.httpProtocol(), builder.jsonFormat());
    slice.resources["broker"] = builder.mqttBroker("broker",builder.mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest, "connectionId2");
    return slice;
};
exports.test_0_5_push_pull = function(){
    let slice = builder.empty_slice("test_0_5_push_pull");
    slice.resources["source"] = builder.source("source", "pull", builder.httpProtocol(), builder.jsonFormat());
    slice.resources["httpdest"] = builder.dest("httpdest", "pull", builder.httpProtocol(), builder.jsonFormat());
    slice.resources["mqttdest"] = builder.dest("mqttdest", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["broker"] = builder.mqttBroker("broker",builder.mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.httpdest, "connectionId2");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.mqttdest, "connectionId3");
    return slice;
};
exports.test_0_6_missing_broker = function(){
    let slice = builder.empty_slice("test_0_6_missing_broker");
    //sidenote: mqtt requires broker but that is not part of the check as of now
    slice.resources["source"] = builder.source("source", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["dest"] = builder.dest("dest", "push", builder.mqttProtocol(), builder.jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest, "connectionId1");
    return slice;
};
exports.test_0_7_missing_broker_and_dataformat_mismatch = function(){
    let slice = builder.empty_slice("test_0_7_missing_broker_and_dataformat_mismatch");
    //sidenote: mqtt requires broker but that is not part of the check as of now
    slice.resources["source"] = builder.source("source", "push", builder.mqttProtocol(), builder.csvFormat());
    slice.resources["dest"] = builder.dest("dest", "push", builder.mqttProtocol(), builder.jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest, "connectionId1");
    return slice;
};
exports.test_0_8_indirect_mismatch_m1 = function(){
    let slice = builder.empty_slice("test_0_8_indirect_mismatch_m1");
    slice.resources["source1"] = builder.source("source1", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["source2"] = builder.source("source2", "push", builder.mqttProtocol(), builder.csvFormat());
    slice.resources["dest"] = builder.dest("dest", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["broker"] = builder.mqttBroker("broker",builder.mqttProtocol());
    util.sliceConnect(slice, slice.resources.source1, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.source2, slice.resources.broker, "connectionId2");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest, "connectionId3");
    return slice;
};
exports.test_0_9_indirect_mismatch_1n = function(){
    let slice = builder.empty_slice("test_0_9_indirect_mismatch_1n");
    slice.resources["source"] = builder.source("source", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["dest1"] = builder.dest("dest1", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["dest2"] = builder.dest("dest2", "push", builder.mqttProtocol(), builder.csvFormat());
    slice.resources["broker"] = builder.mqttBroker("broker",builder.mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest1, "connectionId2");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest2, "connectionId3");
    return slice;
};
exports.test_0_10_indirect_mismatch_mn = function(){
    let slice = builder.empty_slice("test_0_10_indirect_mismatch_mn");
    slice.resources["source1"] = builder.source("source1", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["source2"] = builder.source("source2", "push", builder.mqttProtocol(), builder.csvFormat());
    slice.resources["dest1"] = builder.dest("dest1", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["dest2"] = builder.dest("dest2", "push", builder.mqttProtocol(), builder.csvFormat());
    slice.resources["broker"] = builder.mqttBroker("broker",builder.mqttProtocol());
    util.sliceConnect(slice, slice.resources.source1, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.source2, slice.resources.broker, "connectionId2");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest1, "connectionId3");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest2, "connectionId4");
    return slice;
};


exports.test_0_12_basic_circle = function(){
    let slice = builder.empty_slice("test_0_12_basic_circle");
    let jsonInputs = builder.dest("dest1", "push", builder.httpProtocol(), builder.jsonFormat()).metadata.inputs;
    let csvInputs = builder.dest("dest2", "push", builder.httpProtocol(), builder.csvFormat()).metadata.inputs;
    slice.resources["source1"] = builder.source("source1", "push", builder.httpProtocol(), builder.jsonFormat());
    slice.resources["source2"] = builder.source("source2", "push", builder.httpProtocol(), builder.jsonFormat());
    slice.resources["source3"] = builder.source("source3", "push", builder.httpProtocol(), builder.csvFormat());
    slice.resources.source1.metadata.inputs = util.deepcopy(jsonInputs);
    slice.resources.source2.metadata.inputs = util.deepcopy(jsonInputs);
    slice.resources.source3.metadata.inputs = util.deepcopy(csvInputs);


    util.sliceConnect(slice, slice.resources.source1, slice.resources.source2, "connectionId1");
    util.sliceConnect(slice, slice.resources.source2, slice.resources.source3, "connectionId2");
    util.sliceConnect(slice, slice.resources.source3, slice.resources.source1, "connectionId3");
    return slice;
};

exports.test_0_13_multi_circle = function(){
    let slice = builder.empty_slice("test_0_13_multi_circle");

    slice.resources["source"] = builder.source("source", "push", builder.mqttProtocol(), builder.csvFormat());
    slice.resources["dest"] = builder.dest("dest", "push", builder.mqttProtocol(), builder.jsonFormat());
    slice.resources["broker"] = builder.mqttBroker("broker",builder.mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest, "connectionId2");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.source, "connectionId3");
    util.sliceConnect(slice, slice.resources.dest, slice.resources.broker, "connectionId4");
    return slice;
};

exports.test_2_0_datacontract_reliability = function () {
    let slice = builder.empty_slice("test_2_0_datacontract_reliability");
    slice.resources["source"] = builder.source("source", "push", builder.httpProtocol(), builder.jsonFormat());
    slice.resources["dest"] = builder.dest("dest", "push", builder.httpProtocol(), builder.jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest, "connectionId1");

    slice.resources.source.metadata.resource.qos = {reliability: 89.9};
    slice.resources.dest.metadata.resource.qos = {reliability: 95};
    return slice;
};