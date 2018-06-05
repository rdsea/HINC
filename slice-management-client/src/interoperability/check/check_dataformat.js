exports.checkDataFormat = function (sourceOutput, targetInput, errors, warnings) {


    if(sourceOutput.dataformat.dataformat_name !== targetInput.dataformat.dataformat_name ){
        //TODO warning object
        errors.push({});
    }

};