exports.metadata = {
    resource: exports.resource,
    inputs: exports.inputs,
    outputs: exports.outputs
};


exports.resource = {
    category: categoryValues,
    type: exports.prototype,
    data_contract: exports.data_contract,
    qos: exports.qos,
    qod: exports.qod,
    required_qos: exports.qos,
    required_qod: exports.qod
};

exports.inputs = {
    push_pull:push_pullValues,
    protocol: exports.protocol,
    dataformat: exports.dataformat,
    network_protocol: exports.network_protocol,
    required_qos: exports.qos,
    required_qod: exports.qod
};

exports.outputs = {
    push_pull:push_pullValues,
    protocol: exports.protocol,
    dataformat: exports.dataformat,
    network_protocol: exports.network_protocol,
    qos: exports.qos,
    qod: exports.qod
};

exports.qod = {
    data_interval: decimalType,
    reliability_level: integerType,
    availability: decimalType,
    bit_rate: decimalType,
    bit_rate_unit: stringType,
    connection_limit: integerType
};

exports.qos = {
    completeness: decimalType,
    conformity: decimalType,
    average_message_age: decimalType,
    average_measurement_age: decimalType,
    precision: decimalType
};

exports.data_contract = {
    data_rights:{
        derivation: booleanType,
        collection: booleanType,
        reproduction: booleanType,
        commercial_usage: booleanType,
        attribution: booleanType
    },
    pricing:{
        price: decimalType,
        currency: stringType,
        price_per: stringType
    },
    regulation:{
        jurisdiction: stringType,
        data_regulation_acts: stringType
    }
};

exports.protocol = [amqp(), secured(amqp()), mqtt(), secured(mqtt()), http(), secured(http()), coap(), secured(coap()), stomp()];
exports.dataformat = [apache_avro() ,binary() ,cbor() ,csv() ,json() ,plaintext() ,rdf() ,xml()];
exports.prototype = [container(), firewall(), ingestion(), messagebroker(), sensor(), software_artefact(), storage(), virtual_machine(), vpn()];



exports.network_protocol = [];


//********************************************************************************************************** dataformats

function apache_avro(){
    return {
        encoding:stringType,
        dataformat_name:"apache_avro",
        schema:objectType
    }
}
function binary(){
    return {
        encoding:stringType,
        dataformat_name:"binary"
    }
}
function cbor(){
    return {
        encoding:stringType,
        dataformat_name:"cbor"
    }
}
function csv(){
    return {
        encoding:stringType,
        dataformat_name:"csv",
        seperator:stringType,
        newline_seperator:stringType,
        headers:stringType
    }
}
function json(){
    return {
        encoding:stringType,
        dataformat_name:"json"
    }
}
function plaintext(){
    return {
        encoding:stringType,
        dataformat_name:"plaintext"
    }
}
function rdf(){
    return {
        encoding:stringType,
        dataformat_name:"rdf"
    }
}
function xml(){
    return {
        encoding:stringType,
        dataformat_name:"xml"
    }
}

//********************************************************************************************************** protocols

function amqp(){
    return {
        uri: stringType,
        protocol_name:"amqp",
        version: stringType,
        queue: stringType,
        exchange: stringType,
        implementation: stringType,
        implementation_version: stringType
    };
}

function mqtt(){
    return {
        uri: stringType,
        protocol_name:"mqtt",
        version: stringType,
        topic: stringType,
        qos:mqttQosValues,
        keep_alive:integerType,
        will_required:booleanType,
        will_topic: stringType,
        will_qos:mqttQosValues,
        implementation: stringType,
        implementation_version: stringType
    };
}

function http(){
    return {
        uri: stringType,
        protocol_name:"http",
        http_method:http_methodValues,
        parameters:objectType
    };
}

function coap(){
    return {
        uri: stringType,
        protocol_name:"coap",
        version:stringType,
        method:coap_methodValues,
        parameters:objectType,
        observe:booleanType,
        max_age:integerType,
        observer_limit:integerType,
        implementation:stringType,
        implementation_version:stringType
    };
}

function stomp(){
    return {
        uri: stringType,
        protocol_name:"stomp",
        topic:stringType,
        version:stringType,
        heartbeat:integerType,
        secured:booleanType,
        implementation:stringType,
        implementation_version:stringType
    };
}


//********************************************************************************************************** prototypes

function container(){
    return {
        prototype:"container",
        port_mappings:[objectType],
        container_network:stringType
    }
}
function firewall(){
    return {
        prototype:"firewall",
        rules:[firewallRuleType],
        default_incoming:firewallRuleActionValues,
        default_outgoing:firewallRuleActionValues
    }
}
function ingestion(){
    return {
        prototype:"ingestion",
        product_names: [product_nameValues],
        providers: [providerValues],
        database_types: [database_typeValues]
    }
}
function messagebroker(){
    return {
        prototype:"messagebroker",
        broker: stringType,
        broker_version: stringType,
        protocols:[exports.protocol],
        queues:[brokerQueueType],
        exchanges:[stringType],
        bindings:[brokerBindingType],
        topics:[stringType],
        auto_create: booleanType
    }
}
function sensor(){
    return {
        prototype:"sensor",
        unit:stringType,
        sampling_interval:decimalType,
        precision:decimalType,
        range_minValue:decimalType,
        range_maxValue:decimalType,
        location:stringType,
        longitude:decimalType,
        latitude:decimalType,
        description:stringType,
        semantic_reference: objectType
    }
}
function software_artefact(){
    return {
        prototype:"software_artefact",
        programming_language:programming_languageValues,
        application:applicationValues,
        artefact_type:artefact_typeValues
    }
}
function storage(){
    return {
        prototype:"storage",
        product_name:product_nameValues,
        provider:providerValues,
        database_type:database_typeValues
    }
}
function virtual_machine(){
    return {
        prototype:"virtual_machine",
        provider:providerValues,
        instance_type:vm_instance_typeValues,
        cpu_count:integerType,
        memory:decimalType,
        gpu_count:integerType,
        os:vm_osValues
    }
}
function vpn(){
    return {
        prototype:"vpn",
        type:vpnTypeValues,
        destination:stringType,
        vpn_protocol:stringType
    }
}


//********************************************************************************************************** network_protocols

//********************************************************************************************************** helper
function deepcopy(obj){
    return JSON.parse(JSON.stringify(obj));
}

function secured(protocol){
    let secured = deepcopy(protocol);
    secured.protocol_name += "s";
    return secured;
}

let stringType = "_string";
let decimalType = "_decimal";
let integerType = "_integer";
let booleanType = "_boolean";
let objectType = "_object";

let brokerQueueType = {
    name:stringType,
    durable:booleanType
};

let brokerBindingType = {
    queue:stringType,
    exchange:stringType,
    routing_key:stringType,
    type:bindingTypeValues
};

let firewallRuleType = {
    action:firewallRuleActionValues,
    protocol: stringType,
    port:integerType,
    range:stringType,
    direction:firewallRuleDirectionValues
};

let push_pullValues = ["push", "pull"];
let categoryValues = ["iot", "network_function", "cloud"];
let mqttQosValues = [0, 1, 2];
let http_methodValues = [];
let coap_methodValues = ["GET", "POST", "PUT", "DELETE"];

let product_nameValues = [];
let providerValues = [];
let database_typeValues = [];

let vm_instance_typeValues = [];
let vm_osValues = [];

let programming_languageValues = [];
let applicationValues = [];
let artefact_typeValues = [];

let firewallRuleActionValues = ["allow", "deny"];
let firewallRuleDirectionValues = ["incoming","outgoing"];

let vpnTypeValues = ["end-to-end", "site-to-site", "site-to-end", "end-to-site"];

let bindingTypeValues = ["fanout", "topic", "direct", "headers"];