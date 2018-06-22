const basic = require('./basic_types_values');
const util = require('../util');


exports.protocol = util.createValueDomain([
    amqp(), secured(amqp()), mqtt(), secured(mqtt()),
    http(), secured(http()), coap(), secured(coap()), stomp()]);


//********************************************************************************************************** protocols

function amqp(){
    return {
        uri: basic.stringType,
        protocol_name:"amqp",
        version: basic.stringType,
        queue: basic.stringType,
        exchange: basic.stringType,
        implementation: basic.stringType,
        implementation_version: basic.stringType
    };
}

function mqtt(){
    return {
        uri: basic.stringType,
        protocol_name:"mqtt",
        version: basic.stringType,
        topic: basic.stringType,
        qos: basic.mqttQosValues,
        keep_alive: basic.integerType,
        will_required: basic.booleanType,
        will_topic: basic.stringType,
        will_qos: basic.mqttQosValues,
        implementation: basic.stringType,
        implementation_version: basic.stringType
    };
}

function http(){
    return {
        uri: basic.stringType,
        protocol_name:"http",
        http_method: basic.http_methodValues,
        parameters: basic.objectType
    };
}

function coap(){
    return {
        uri: basic.stringType,
        protocol_name:"coap",
        version: basic.stringType,
        method: basic.coap_methodValues,
        parameters: basic.objectType,
        observe: basic.booleanType,
        max_age: basic.integerType,
        observer_limit: basic.integerType,
        implementation: basic.stringType,
        implementation_version: basic.stringType
    };
}

function stomp(){
    return {
        uri: basic.stringType,
        protocol_name:"stomp",
        topic: basic.stringType,
        version: basic.stringType,
        heartbeat: basic.integerType,
        secured: basic.booleanType,
        implementation: basic.stringType,
        implementation_version: basic.stringType
    };
}



function secured(protocol){
    let secured = util.deepcopy(protocol);
    secured.protocol_name += "s";
    return secured;
}