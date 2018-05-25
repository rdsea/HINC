const basic = require('./basic_types_values');
const util = require('../util');

const protocol_d = require('./protocol_domain');

exports.a_prototype = util.createValueDomain(
    [container(), firewall(), ingestion(), messagebroker(),
        sensor(), software_artefact(), storage(), virtual_machine(), vpn()]);



//********************************************************************************************************** prototypes

function container(){
    return {
        prototype:"container",
        port_mappings:[basic.objectType],
        container_network:basic.stringType
    }
}
function firewall(){
    return {
        prototype:"firewall",
        rules:[basic.firewallRuleType],
        default_incoming:basic.firewallRuleActionValues,
        default_outgoing:basic.firewallRuleActionValues
    }
}
function ingestion(){
    return {
        prototype:"ingestion",
        product_names: [basic.product_nameValues],
        providers: [basic.providerValues],
        database_types: [basic.database_typeValues]
    }
}
function messagebroker(){
    return {
        prototype:"messagebroker",
        broker: basic.stringType,
        broker_version: basic.stringType,
        protocols:[protocol_d.protocol],
        queues:[basic.brokerQueueType],
        exchanges:[basic.stringType],
        bindings:[basic.brokerBindingType],
        topics:[basic.stringType],
        auto_create: basic.booleanType
    }
}
function sensor(){
    return {
        prototype:"sensor",
        unit:basic.stringType,
        sampling_interval:basic.decimalType,
        precision:basic.decimalType,
        range_minValue:basic.decimalType,
        range_maxValue:basic.decimalType,
        location:basic.stringType,
        longitude:basic.decimalType,
        latitude:basic.decimalType,
        description:basic.stringType,
        semantic_reference: basic.objectType
    }
}
function software_artefact(){
    return {
        prototype:"software_artefact",
        programming_language:basic.programming_languageValues,
        application:basic.applicationValues,
        artefact_type:basic.artefact_typeValues
    }
}
function storage(){
    return {
        prototype:"storage",
        product_name:basic.product_nameValues,
        provider:basic.providerValues,
        database_type:basic.database_typeValues
    }
}
function virtual_machine(){
    return {
        prototype:"virtual_machine",
        provider:basic.providerValues,
        instance_type:basic.vm_instance_typeValues,
        cpu_count:basic.integerType,
        memory:basic.decimalType,
        gpu_count:basic.integerType,
        os:basic.vm_osValues
    }
}
function vpn(){
    return {
        prototype:"vpn",
        type:basic.vpnTypeValues,
        destination:basic.stringType,
        vpn_protocol:basic.stringType
    }
}
