const util = require('../../../main/util/slice_util');


exports.test_0_working_slice = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "push", httpProtocol(), jsonFormat());
    slice.resources["dest"] = dest("dest", "push", httpProtocol(), jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest, "connectionId1");
    return slice;
};
exports.test_1_direct_mismatch = function(){
    let slice = empty_slice();
    //sidenote: mqtt requires broker but that is not part of the check as of now
    slice.resources["source"] = source("source", "push", httpProtocol(), csvFormat());
    slice.resources["dest"] = dest("dest", "push", httpProtocol(), jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest, "connectionId1");
    return slice;
};
exports.test_2_indirect_mismatch = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "push", mqttProtocol(), csvFormat());
    slice.resources["dest"] = dest("dest", "push", mqttProtocol(), jsonFormat());
    slice.resources["broker"] = mqttBroker("broker",mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest, "connectionId2");
    return slice;
};
exports.test_3_substitution = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "push", mqttProtocol(), jsonFormat());
    slice.resources["dest"] = dest("dest", "push", mqttProtocol(), jsonFormat());
    slice.resources["oldbroker"] = amqpBroker("oldbroker",amqpProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.oldbroker, "connectionId1");
    util.sliceConnect(slice, slice.resources.oldbroker, slice.resources.dest, "connectionId2");
    return slice;
};
exports.test_4_reduction = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "pull", httpProtocol(), jsonFormat());
    slice.resources["dest"] = dest("dest", "pull", httpProtocol(), jsonFormat());
    slice.resources["broker"] = mqttBroker("broker",mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest, "connectionId2");
    return slice;
};
exports.test_5_push_pull = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "pull", httpProtocol(), jsonFormat());
    slice.resources["httpdest"] = dest("httpdest", "pull", httpProtocol(), jsonFormat());
    slice.resources["mqttdest"] = dest("mqttdest", "push", mqttProtocol(), jsonFormat());
    slice.resources["broker"] = mqttBroker("broker",mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.httpdest, "connectionId2");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.mqttdest, "connectionId3");
    return slice;
};
exports.test_6_missing_broker = function(){
    let slice = empty_slice();
    //sidenote: mqtt requires broker but that is not part of the check as of now
    slice.resources["source"] = source("source", "push", mqttProtocol(), jsonFormat());
    slice.resources["dest"] = dest("dest", "push", mqttProtocol(), jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest, "connectionId1");
    return slice;
};
exports.test_7_missing_broker_and_dataformat_mismatch = function(){
    let slice = empty_slice();
    //sidenote: mqtt requires broker but that is not part of the check as of now
    slice.resources["source"] = source("source", "push", mqttProtocol(), csvFormat());
    slice.resources["dest"] = dest("dest", "push", mqttProtocol(), jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest, "connectionId1");
    return slice;
};
exports.test_8_indirect_mismatch_m1 = function(){
    let slice = empty_slice();
    slice.resources["source1"] = source("source1", "push", mqttProtocol(), jsonFormat());
    slice.resources["source2"] = source("source2", "push", mqttProtocol(), csvFormat());
    slice.resources["dest"] = dest("dest", "push", mqttProtocol(), jsonFormat());
    slice.resources["broker"] = mqttBroker("broker",mqttProtocol());
    util.sliceConnect(slice, slice.resources.source1, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.source2, slice.resources.broker, "connectionId2");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest, "connectionId3");
    return slice;
};
exports.test_9_indirect_mismatch_1n = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "push", mqttProtocol(), jsonFormat());
    slice.resources["dest1"] = dest("dest1", "push", mqttProtocol(), jsonFormat());
    slice.resources["dest2"] = dest("dest2", "push", mqttProtocol(), csvFormat());
    slice.resources["broker"] = mqttBroker("broker",mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest1, "connectionId2");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest2, "connectionId3");
    return slice;
};
exports.test_10_indirect_mismatch_mn = function(){
    let slice = empty_slice();
    slice.resources["source1"] = source("source1", "push", mqttProtocol(), jsonFormat());
    slice.resources["source2"] = source("source2", "push", mqttProtocol(), csvFormat());
    slice.resources["dest1"] = dest("dest1", "push", mqttProtocol(), jsonFormat());
    slice.resources["dest2"] = dest("dest2", "push", mqttProtocol(), csvFormat());
    slice.resources["broker"] = mqttBroker("broker",mqttProtocol());
    util.sliceConnect(slice, slice.resources.source1, slice.resources.broker, "connectionId1");
    util.sliceConnect(slice, slice.resources.source2, slice.resources.broker, "connectionId2");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest1, "connectionId3");
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest2, "connectionId4");
    return slice;
};

function empty_slice(){
    return {resources:{},connectivities:{}};
}

function source(name, push_pull, protocol, format){
    return {
        name:name,
        source: [],
        target: [],
        metadata:{
            resource:{category:"iot", type:{prototype:"sensor"}},
            inputs: [],
            outputs:[
                {
                    push_pull:push_pull,
                    protocol:protocol,
                    dataformat:format
                }
            ]
        }
    };
}

function dest(name, push_pull, protocol, format){
    return {
        name:name,
        source: [],
        target: [],
        metadata:{
            resource:{category:"iot", type:{prototype:"storage"}},
            inputs:[
                {
                    push_pull:push_pull,
                    protocol:protocol,
                    dataformat:format
                }
            ],
            outputs:[]
        }
    };
}

function mqttBroker(name, mqttprotocol){
    return {
        name:name,
        source:[],
        target:[],
        metadata: {
            resource: {
                category:"network_function",
                type:{
                prototype: "messagebroker",
                protocols: [mqttprotocol],
                topics: [mqttprotocol.topic]
                }
            }
        }
    }
}



function amqpBroker(name, amqpprotocol){
    return {
        name:name,
        source:[],
        target:[],
        metadata: {
            resource: {
                category:"network_function",
                type: {
                    prototype: "messagebroker",
                    protocols: [amqpprotocol],
                    queues: [amqpprotocol.queue],
                    exchanges: [amqpprotocol.exchange]
                }
            }
        }
    }
}

function mqttProtocol(){
    return {uri:"mqtt://test:1883",
        protocol_name:"mqtt",
        topic:"basic_test",
        qos:0}
}


function amqpProtocol() {
    return {
        uri:"amqp://test:5672",
        protocol_name:"amqp",
        queue:"testqueue",
        exchange:"testexchange",
    }
}


function httpProtocol(){
    return {
        uri: "http://test:80",
        protocol_name: "http",
        http_method: "get"
    }
}

function csvFormat() {
    return {encoding:"utf-8",
        dataformat_name:"csv"}
}

function jsonFormat() {
    return {encoding:"utf-8",
            dataformat_name:"json"}
}