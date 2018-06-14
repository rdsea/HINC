const errorGenerator = require('../transform/error_generator_graph');


exports.checkDataFormat = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {


    if(sourceMetadata.resource.category === "network_function" || targetMetadata.resource.category === "network_function"){
        return;
    }

    if(sourceOutput.dataformat.dataformat_name !== targetInput.dataformat.dataformat_name ){
        //TODO warning object
        errors.push({});
    }

};