

exports.check = function (slice, additionalCompare) {
    if (additionalCompare == null){
        additionalCompare = function(a,b){return true};
    }

    return mandatoryCompare(slice[0],slice[2]) && additionalCompare(slice[0], slice[2]);

};


function mandatoryCompare(from, to) {
    if(from.metadata.output[0].protocol !== to.metadata.input[0].protocol){
        return false;
    }
    return true;
}



exports.falseCompare = function (from, to) {
    return false;
};