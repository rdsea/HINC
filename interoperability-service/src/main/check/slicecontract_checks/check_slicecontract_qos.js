
exports.checkSliceContractQos = function (node, nodeMetadata, datacontract, contract_violations) {
    reliability(node, nodeMetadata, datacontract, contract_violations);
    message_frequency(node, nodeMetadata, datacontract, contract_violations);
};


function reliability(node, nodeMetadata, datacontract, contract_violations) {
    try {
        if (nodeMetadata.resource.qos.reliability < datacontract.qos.reliability) {
            contract_violations.push({
                nodename: node.nodename,
                key: "metadata.resource.qos.reliability",
                value: nodeMetadata.resource.qos.reliability
            })
        }
    }catch (e) {

    }
}

function message_frequency(node, nodeMetadata, datacontract, contract_violations) {
    try {
        if (nodeMetadata.outputs[0].qos.message_frequency > datacontract.qos.message_frequency) {
            contract_violations.push({
                nodename: node.nodename,
                key: "metadata.outputs[0].qos.message_frequency",
                value: nodeMetadata.outputs[0].qos.message_frequency
            })
        }
    }catch (e) {

    }
}