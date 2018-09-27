const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const Pricing = new Schema({
    price:Number,
    currency:String,
    unit:String
},{_id:false});

module.exports = {
    Pricing:Pricing
};