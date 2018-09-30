const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const QoData = new Schema({
    completeness:Number,
    conformity:Number,
    average_message_age:Number,
    average_measurement_age:Number,
    precision:Number
},{_id:false});

module.exports = {
    QoData:QoData
};