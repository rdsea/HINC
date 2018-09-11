const basic = require('./basic_types_values');
const util = require('../util');



exports.data_contract = {
    data_rights:{
        derivation: basic.booleanType,
        collection: basic.booleanType,
        reproduction: basic.booleanType,
        commercial_usage: basic.booleanType,
        attribution: basic.booleanType
    },
    pricing:{
        price: basic.decimalType,
        currency: basic.stringType,
        price_per: basic.stringType
    },
    regulation:{
        jurisdiction: basic.stringType,
        data_regulation_acts: basic.stringType
    }
};