const errorGenerator = require('../../transform/error_generator_graph');


exports.checkPerformanceMetadataIndirect = function (sourceMetadata, sourceOutput, targetMetadata, targetInput, errors, warnings) {
    try{
        if(sourceOutput.performance_indirect && targetInput.performance_indirect){
            let minCount = sourceOutput.performance_indirect.count;
            if(targetInput.performance_indirect<minCount){
                minCount=targetInput.performance_indirect;
            }
            for(let i = 0; i<sourceOutput.performance_indirect.count; i++){
                if(sourceOutput.performance_indirect[`indirect_performance_${i}`] !== targetInput.performance_indirect[`indirect_performance_${i}`]){
                    errors.push({
                        key: `metadata.inputs.performance_indirect.indirect_performance_${i}`,
                        value: sourceOutput.performance_indirect[`indirect_performance_${i}`]
                    });
                    errors.push({
                        key: `metadata.outputs.performance_indirect.indirect_performance_${i}`,
                        value: targetInput.performance_indirect[`indirect_performance_${i}`]
                    });
                }
            }
        }
    }catch(e)
    {
    }
};