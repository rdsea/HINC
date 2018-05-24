const basic = require('./basic_types_values');
const util = require('../util');


exports.qod = {
    data_interval: basic.decimalType,
    reliability_level: basic.integerType,
    availability: basic.decimalType,
    bit_rate: basic.decimalType,
    bit_rate_unit: basic.stringType,
    connection_limit: basic.integerType
};

exports.qos = {
    completeness: basic.decimalType,
    conformity: basic.decimalType,
    average_message_age: basic.decimalType,
    average_measurement_age: basic.decimalType,
    precision: basic.decimalType
};
