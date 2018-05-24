exports.deepcopy = function(obj){
    return JSON.parse(JSON.stringify(obj));
};