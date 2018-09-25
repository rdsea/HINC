const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const Metadata = new Schema({
    resource:String,
    inputs:[String],
    outputs:[String]
},{_id:false});

module.exports = {
    Metadata:Metadata
};