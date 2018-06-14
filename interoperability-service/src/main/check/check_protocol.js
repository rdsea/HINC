const errorGenerator = require('../transform/error_generator_graph');


exports.checkProtocols = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {
    if(sourceOutput.protocol.protocol_name !== targetInput.protocol.protocol_name){
        errors.push("protocol.protocol_name");
    }

    if(sourceOutput.push_pull !== targetInput.push_pull){
        errors.push("push_pull");
    }

    if(sourceOutput.protocol.qos !== targetInput.protocol.qos){
        warnings.push("protocol.qos");
    }
};