const domain = require('./domain/metadata_domain');
const util = require('./util');
const basic = require('./domain/basic_types_values')

exports.randomResource = function(){
    let metadata = util.deepcopy(domain.metadata);

    metadata.resource.category = basic.categoryValues[0];

    return metadata;
};