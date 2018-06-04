const util = require('../../util/slice_util');


exports.test_0_working_slice = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "push", mqttProtocol(), jsonFormat());
    slice.resources["dest"] = dest("dest", "push", mqttProtocol(), jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest);
    return slice;
};
exports.test_1_direct_mismatch = function(){
    let slice = empty_slice();
    //sidenote: mqtt requires broker but that is not part of the check as of now
    slice.resources["source"] = source("source", "push", mqttProtocol(), csvFormat());
    slice.resources["dest"] = dest("dest", "push", mqttProtocol(), jsonFormat());
    util.sliceConnect(slice, slice.resources.source, slice.resources.dest);
    return slice;
};
exports.test_2_indirect_mismatch = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "push", mqttProtocol(), csvFormat());
    slice.resources["dest"] = dest("dest", "push", mqttProtocol(), jsonFormat());
    slice.resources["broker"] = mqttBroker("broker",mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker);
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest);
    return slice;
};
exports.test_3_substitution = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "push", mqttProtocol(), jsonFormat());
    slice.resources["dest"] = dest("dest", "push", mqttProtocol(), jsonFormat());
    slice.resources["broker"] = amqpBroker("broker",amqpProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker);
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest);
    return slice;
};
exports.test_4_reduction = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "pull", httpProtocol(), jsonFormat());
    slice.resources["dest"] = dest("dest", "pull", httpProtocol(), jsonFormat());
    slice.resources["broker"] = mqttBroker("broker",mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker);
    util.sliceConnect(slice, slice.resources.broker, slice.resources.dest);
    return slice;
};
exports.test_5_push_pull = function(){
    let slice = empty_slice();
    slice.resources["source"] = source("source", "pull", httpProtocol(), jsonFormat());
    slice.resources["httpdest"] = dest("httpdest", "pull", httpProtocol(), jsonFormat());
    slice.resources["mqttdest"] = dest("mqttdest", "push", mqttProtocol(), jsonFormat());
    slice.resources["broker"] = mqttBroker("broker",mqttProtocol());
    util.sliceConnect(slice, slice.resources.source, slice.resources.broker);
    util.sliceConnect(slice, slice.resources.broker, slice.resources.httpdest);
    util.sliceConnect(slice, slice.resources.broker, slice.resources.mqttdest);
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
            resource:{category:"iot"},
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
            resource:{category:"iot"},
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
                category:"network_funciton",
                prototype: "messagebroker",
                protocols: [mqttprotocol],
                topics: [mqttprotocol.topic]
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
                category:"network_funciton",
                prototype: "messagebroker",
                protocols: [amqpprotocol],
                queues:[amqpprotocol.queue],
                exchanges:[amqpprotocol.exchange]
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