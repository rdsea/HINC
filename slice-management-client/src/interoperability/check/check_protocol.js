const errorGenerator = require('../transform/error_generator_graph');


exports.checkProtocols = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {
    /*source = connection.source;
    target = connection.target;

    console.log("check " + connection.connectionId);
    console.log("source.metadata.output[0].protocol " + source.metadata.output[0].protocol);
    console.log("target.metadata.input[0].protocol " + target.metadata.input[0].protocol);
    if (source.metadata.output[0].protocol !== target.metadata.input[0].protocol) {
        problems.push(source.metadata.id + " </> " + target.metadata.id );
    }*/

    if(sourceOutput.push_pull !== targetInput.push_pull){
        //TODO warning object
        errors.push({});
    }

    if(sourceOutput.protocol.qos !== targetInput.protocol.qos){
        warnings.push({});
    }
};