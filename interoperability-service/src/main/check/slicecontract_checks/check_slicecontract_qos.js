
exports.checkSliceContractQos = function (node, nodeMetadata, datacontract, contract_violations) {

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

};