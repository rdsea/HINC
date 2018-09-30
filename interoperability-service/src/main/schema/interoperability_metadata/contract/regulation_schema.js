const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const Regulation = new Schema({
    jurisdiction:String,
    data_regulation_acts:[String]
},{_id:false});

module.exports = {
    Regulation:Regulation
};