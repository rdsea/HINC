
exports.stringType = "_string";
exports.decimalType = "_decimal";
exports.integerType = "_integer";
exports.booleanType = "_boolean";
exports.objectType = "_object";

exports.brokerQueueType = {
    name:exports.stringType,
    durable:exports.booleanType
};

exports.brokerBindingType = {
    queue:exports.stringType,
    exchange:exports.stringType,
    routing_key:exports.stringType,
    type:exports.bindingTypeValues
};

exports.firewallRuleType = {
    action:exports.firewallRuleActionValues,
    protocol:exports.stringType,
    port:exports.integerType,
    range:exports.stringType,
    direction:exports.firewallRuleDirectionValues
};

exports.push_pullValues = {_domain_values:["push", "pull"]};
exports.categoryValues = {_domain_values:["iot", "network_function", "cloud"]};
exports.mqttQosValues = {_domain_values:[0, 1, 2]};
exports.http_methodValues = {_domain_values:[]};
exports.coap_methodValues = {_domain_values:["GET", "POST", "PUT", "DELETE"]};
exports.product_nameValues = {_domain_values:[]};
exports.providerValues = {_domain_values:[]};
exports.database_typeValues = {_domain_values:[]};
exports.vm_instance_typeValues = {_domain_values:[]};
exports.vm_osValues = {_domain_values:[]};
exports.programming_languageValues = {_domain_values:[]};
exports.applicationValues = {_domain_values:[]};
exports.artefact_typeValues = {_domain_values:[]};
exports.firewallRuleActionValues = {_domain_values:["allow", "deny"]};
exports.firewallRuleDirectionValues = {_domain_values:["incoming","outgoing"]};
exports.vpnTypeValues = {_domain_values:["end-to-end", "site-to-site", "site-to-end", "end-to-site"]};
exports.bindingTypeValues = {_domain_values:["fanout", "topic", "direct", "headers"]};

exports.domain_values_identifier = "_domain_values";

exports.createValueDomain = function (valuesArray){
    return {_domain_values:valuesArray};
};
