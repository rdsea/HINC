const basic = require('./domain/basic_types_values')

exports.deepcopy = function(obj){
    return JSON.parse(JSON.stringify(obj));
};

exports.createValueDomain = function(valueArray){
    return basic.createValueDomain(valueArray);
};