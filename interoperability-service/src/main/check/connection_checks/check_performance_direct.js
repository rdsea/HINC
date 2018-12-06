const errorGenerator = require('../../transform/error_generator_graph');


exports.checkPerformanceMetadataIndirect = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {
    try{
        if(sourceOutput.performance_direct && targetInput.performance_direct){
            let minCount = sourceOutput.performance_direct.count;
            if(targetInput.performance_direct.count<minCount){
                minCount=targetInput.performance_direct.count;
            }
            for(let i = 0; i<minCount; i++){
                if(sourceOutput.performance_direct[`direct_performance_${i}`] !== targetInput.performance_direct[`direct_performance_${i}`]){
                    errors.push({
                        key: `metadata.inputs.performance_direct.direct_performance_${i}`,
                        value: sourceOutput.performance_direct[`direct_performance_${i}`]
                    });
                    errors.push({
                        key: `metadata.outputs.performance_direct.direct_performance_${i}`,
                        value: targetInput.performance_direct[`direct_performance_${i}`]
                    });
                }
            }
        }
    }catch(e)
    {
    }
};