
exports.checkSliceContractJurisdiction = function (node, nodeMetadata, datacontract, contract_violations) {
    try {
        if(datacontract.data_contract.regulation.jurisdiction.indexOf(nodeMetadata.resource.data_contract.regulation.jurisdiction) === -1){
            contract_violations.push({
                nodename: node.nodename,
                key: "metadata.resource.data_contract.regulation.jurisdiction",
                value: nodeMetadata.resource.data_contract.regulation.jurisdiction
            })
        }
    }catch (e) {

    }

};