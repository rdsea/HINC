
exports.checkSliceContractQod = function (node, nodeMetadata, datacontract, contract_violations) {

    precision(node, nodeMetadata, datacontract, contract_violations);
    average_measurement_age(node, nodeMetadata, datacontract, contract_violations);

};


function precision(node, nodeMetadata, datacontract, contract_violations) {
    try {
        if (nodeMetadata.resource.qod.precision > datacontract.qod.precision) {
            contract_violations.push({
                nodename: node.nodename,
                key: "metadata.resource.qos.precision ",
                value: nodeMetadata.resource.qod.precision
            })
        }
    }catch (e) {

    }
}

function average_measurement_age(node, nodeMetadata, datacontract, contract_violations) {
    try {
        if (nodeMetadata.outputs[0].qod.average_measurement_age > datacontract.qod.average_measurement_age) {
            contract_violations.push({
                nodename: node.nodename,
                key: "metadata.outputs[0].qod.average_measurement_age ",
                value: nodeMetadata.outputs[0].qod.average_measurement_age
            })
        }
    }catch (e) {

    }
}