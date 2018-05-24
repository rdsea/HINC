
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
    protocol:exports. stringType,
    port:exports.integerType,
    range:exports.stringType,
    direction:exports.firewallRuleDirectionValues
};

exports.push_pullValues = ["push", "pull"];
exports.categoryValues = ["iot", "network_function", "cloud"];
exports.mqttQosValues = [0, 1, 2];
exports.http_methodValues = [];
exports.coap_methodValues = ["GET", "POST", "PUT", "DELETE"];

exports.product_nameValues = [];
exports.providerValues = [];
exports.database_typeValues = [];

exports.vm_instance_typeValues = [];
exports.vm_osValues = [];

exports.programming_languageValues = [];
exports.applicationValues = [];
exports.artefact_typeValues = [];

exports.firewallRuleActionValues = ["allow", "deny"];
exports.firewallRuleDirectionValues = ["incoming","outgoing"];

exports.vpnTypeValues = ["end-to-end", "site-to-site", "site-to-end", "end-to-site"];

exports.bindingTypeValues = ["fanout", "topic", "direct", "headers"];