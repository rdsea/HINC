const errorGenerator = require('../transform/error_generator_graph');


exports.checkProtocols = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {
    //TODO structural error if amqp and mqtt do not communicate via broker)
    if(sourceOutput.protocol.protocol_name === targetInput.protocol.protocol_name) {
        if(arrayContains(["mqtt", "mqtts", "amqp", "amqps"],sourceOutput.protocol.protocol_name)){
            if(sourceMetadata.resource.type.prototype !== "messagebroker" && targetMetadata.resource.type.prototype !== "messagebroker") {
                errors.push("structure_broker_missing");
            }
        }
    }

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

function arrayContains(array, value){
    return array.indexOf(value)>-1;
}