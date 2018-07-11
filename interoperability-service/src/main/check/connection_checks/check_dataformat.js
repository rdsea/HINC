const errorGenerator = require('../../transform/error_generator_graph');


exports.checkDataFormat = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {


    if(sourceMetadata.resource.category === "network_function" || targetMetadata.resource.category === "network_function"){
        return;
    }

    if(sourceOutput.dataformat && targetInput.dataformat) {
        if (sourceOutput.dataformat.dataformat_name !== targetInput.dataformat.dataformat_name) {
            errors.push({
                key: "metadata.inputs.dataformat.dataformat_name",
                value: sourceOutput.dataformat.dataformat_name
            });
            errors.push({
                key: "metadata.outputs.dataformat.dataformat_name",
                value: targetInput.dataformat.dataformat_name
            });
        }
    }

};