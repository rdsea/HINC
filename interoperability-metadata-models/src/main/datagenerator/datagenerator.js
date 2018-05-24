const domain = require('./domain/metadata_domain');
const util = require('./util');
const basic = require('./domain/basic_types_values')

exports.randomResource = function(){
    let metadata = util.deepcopy(domain.metadata);

    metadata.resource.category = basic.categoryValues[0];

    return metadata;
};


exports.randomInteger = function(maxvalue){
    let random = Math.random();

    return Math.round(random*maxvalue);
};

exports.randomDecimal = function(maxvalue){
    let random = Math.random();

    return random*maxvalue;
};

exports.randomBoolean = function(){
    let random = Math.random();
    return random<0.5;
};

exports.randomString = function(){
    return Math.random().toString(36).substr(2);

};