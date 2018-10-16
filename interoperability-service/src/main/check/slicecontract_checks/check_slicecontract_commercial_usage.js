
exports.checkSliceContractCommercialUsage = function (node, nodeMetadata, datacontract, contract_violations) {

    try {
        if(datacontract.data_contract.data_rights.commercial_usage === true) {
            if (nodeMetadata.resource.data_contract.data_rights.commercial_usage === false) {
                contract_violations.push({
                    nodename: node.nodename,
                    key: "metadata.resource.data_contract.data_rights.commercial_usage",
                    value: nodeMetadata.resource.data_contract.data_rights.commercial_usage
                })
            }
        }
    }catch (e) {

    }

};