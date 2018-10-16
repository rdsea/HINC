const errorGenerator = require('../../transform/error_generator_graph');


exports.checkJurisdiction = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {

    try {
        if (sourceMetadata.resource.required.data_contract.jurisdiction !== undefined) {
            if (sourceMetadata.resource.required.data_contract.jurisdiction.indexOf(targetMetadata.resource.data_contract.jurisdiction) === -1) {
                errors.push({
                    key: "sourceMetadata.resource.required.data_contract.jurisdiction",
                    value: sourceMetadata.resource.required.data_contract.jurisdiction
                });
                errors.push({
                    key: "targetMetadata.resource.data_contract.jurisdiction",
                    value: targetMetadata.resource.data_contract.jurisdiction
                });
            }
        }
    }catch (e) {
        
    }
};
