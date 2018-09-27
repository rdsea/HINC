const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const SliceConnectivity = new Schema({
    accessPoint: Schema.Types.Mixed,
    dataFormat:String,
    in:{label:String, accessPoint: Number},
    out:{label:String, accessPoint: Number}
},{_id:false});

module.exports = {
    SliceConnectivity:SliceConnectivity
};