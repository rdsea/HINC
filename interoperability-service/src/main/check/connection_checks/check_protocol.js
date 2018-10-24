const errorGenerator = require('../../transform/error_generator_graph');


exports.checkProtocols = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {
    //TODO structural error if amqp and mqtt do not communicate via broker)
    try {
        if (sourceOutput.protocol.protocol_name === targetInput.protocol.protocol_name) {
            if (arrayContains(["mqtt", "mqtts", "amqp", "amqps"], sourceOutput.protocol.protocol_name)) {
                if (sourceMetadata.resource.type.prototype !== "messagebroker" && targetMetadata.resource.type.prototype !== "messagebroker") {
                    errors.push({key: "metadata.resource.type.prototype", value: "messagebroker"});
                    errors.push({
                        key: "metadata.resource.type.protocols.protocol_name",
                        value: sourceOutput.protocol.protocol_name
                    });
                }
            }
        }
    }catch (e) {
        
    }

    if(sourceOutput.protocol.protocol_name !== targetInput.protocol.protocol_name){
        errors.push({key:"metadata.inputs.protocol.protocol_name", value:sourceOutput.protocol.protocol_name});
        errors.push({key:"metadata.outputs.protocol.protocol_name", value:targetInput.protocol.protocol_name});
    }

    if(sourceOutput.push_pull !== targetInput.push_pull){
        errors.push({key:"metadata.inputs.push_pull", value:sourceOutput.push_pull});
        errors.push({key:"metadata.outputs.push_pull", value:targetInput.push_pull});
    }

    try {
        if (sourceOutput.protocol.qos !== targetInput.protocol.qos) {
            warnings.push({key: "metadata.inputs.protocol.qos", value: sourceOutput.protocol.qos});
            warnings.push({key: "metadata.outputs.protocol.qos", value: targetInput.protocol.qos});
        }
    }catch (e) {

    }
};

function arrayContains(array, value){
    return array.indexOf(value)>-1;
}