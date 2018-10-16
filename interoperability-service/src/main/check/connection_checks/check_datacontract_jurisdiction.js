const errorGenerator = require('../../transform/error_generator_graph');


exports.checkJurisdiction = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {

    try {
        if (sourceMetadata.resource.required.data_contract.regulation.jurisdiction !== undefined) {
            if (sourceMetadata.resource.required.data_contract.regulation.jurisdiction.indexOf(targetMetadata.resource.data_contract.regulation.jurisdiction) === -1) {
                errors.push({
                    key: "sourceMetadata.resource.required.data_contract.regulation.jurisdiction",
                    value: sourceMetadata.resource.required.data_contract.regulation.jurisdiction
                });
                errors.push({
                    key: "targetMetadata.resource.data_contract.regulation.jurisdiction",
                    value: targetMetadata.resource.data_contract.regulation.jurisdiction
                });
            }
        }
    }catch (e) {
        
    }
};
