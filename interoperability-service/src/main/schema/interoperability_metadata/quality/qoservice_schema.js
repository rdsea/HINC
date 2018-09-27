const mongoose = require("mongoose");
const Schema = mongoose.Schema;


const QoService = new Schema({
    data_interval: Number,
    reliability: Number,
    availability: Number,
    bit_rate: Number,
    bit_rate_unit: String,
    connection_limit: Number
},{_id:false});

module.exports = {
    QoService:QoService
};