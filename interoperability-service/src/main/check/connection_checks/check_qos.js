const errorGenerator = require('../../transform/error_generator_graph');


exports.checkQoS = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {
    try {
        if (sourceOutput.qos.message_frequency > targetMetadata.resource.qos.message_frequency) {

            errors.push({
                key: "sourceOutput.qos.message_frequency",
                value: sourceOutput.qos.message_frequency
            });
            errors.push({
                key: "targetMetadata.resource.qos.message_frequency",
                value: targetMetadata.resource.qos.message_frequency
            });

        }
    }catch (e) {

    }
};
