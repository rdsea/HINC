exports.metadata = {
    resource: exports.resource,
    inputs: exports.inputs,
    outputs: exports.outputs
};


exports.resource = {
    category:["iot", "network_function", "cloud"],
    type: exports.prototype,
    data_contract: exports.data_contract,
    qos: exports.qos,
    qod: exports.qod,
    required_qos: exports.qos,
    required_qod: exports.qod
};

exports.inputs = {
    push_pull:["push", "pull"],
    protocol: exports.protocol,
    dataformat: exports.dataformat,
    network_protocol: "",
    required_qos: exports.qos,
    required_qod: exports.qod
};

exports.outputs = {
    push_pull:["push", "pull"],
    protocol: exports.protocol,
    dataformat: exports.dataformat,
    network_protocol: "",
    qos: exports.qos,
    qod: exports.qod
};

//TODO finish
exports.qod = {

};

exports.qos = {

};

exports.protocol = [];
exports.dataformat = [];
exports.data_contract = {

};
exports.prototype = [];
exports.network_protocol = [];