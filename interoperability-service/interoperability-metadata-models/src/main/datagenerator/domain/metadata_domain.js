const basic = require('./basic_types_values');
const util = require('../util');

const data_contract_d = require('./data_contract_domain');
const dataformat_d = require('./dataformat_domain');
const network_protocol_d = require('./network_protocol_domain');
const protocol_d = require('./protocol_domain');
const prototype_d = require('./prototype_domain');
const quality_d = require('./quality_domain');

exports.metadata = {
    resource: resource(),
    inputs: inputs(),
    outputs: outputs()
};

exports.inputs = inputs();

function resource () {
    return {
        category: basic.categoryValues,
        type: prototype_d.a_prototype,
        data_contract: data_contract_d.data_contract,
        qos: quality_d.qos,
        qod: quality_d.qod,
        required_qos: quality_d.qos,
        required_qod: quality_d.qod
    }
};

function inputs() {
    return [inputDefinition()];
}

function inputDefinition() {
    return {
        push_pull:basic.push_pullValues,
        protocol: protocol_d.protocol,
        dataformat: dataformat_d.dataformat,
        network_protocol: network_protocol_d.network_protocol,
        required_qos: quality_d.qos,
        required_qod: quality_d.qod
    }
};

function outputs() {
    return [outputDefinition()];
}

function outputDefinition(){
    return {
        push_pull:basic.push_pullValues,
        protocol: protocol_d.protocol,
        dataformat: dataformat_d.dataformat,
        network_protocol: network_protocol_d.network_protocol,
        qos: quality_d.qos,
        qod: quality_d.qod
    }
}
