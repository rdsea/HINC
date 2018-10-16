
exports.checkSliceContractPricing = function (node, nodeMetadata, datacontract, contract_violations) {
    try {
        if(nodeMetadata.resource.data_contract.pricing.unit === datacontract.data_contract.pricing.unit){
            if(nodeMetadata.resource.data_contract.pricing.currency === datacontract.data_contract.pricing.currency){
                if(nodeMetadata.resource.data_contract.pricing.price > datacontract.data_contract.pricing.price){
                    contract_violations.push({
                        nodename: node.nodename,
                        key: "metadata.resource.data_contract.pricing",
                        value: nodeMetadata.resource.data_contract.pricing
                    })
                }
            }
        }
    }catch (e) {

    }

};