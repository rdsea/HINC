const errorGenerator = require('../../transform/error_generator_graph');

exports.checkQoD = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {
    try {
        if (targetMetadata.resource.required.qod.precision !== undefined) {
            if (targetMetadata.resource.required.qod.precision < sourceMetadata.resource.qod.precision) {
                errors.push({
                    key: "targetMetadata.resource.required.qod.precision",
                    value: targetMetadata.resource.required.qod.precision
                });
                errors.push({
                    key: "sourceMetadata.resource.qod.precision",
                    value: sourceMetadata.resource.qod.precision
                });
            }
        }
    }catch (e) {

    }
};