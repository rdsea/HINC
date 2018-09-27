const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const SliceConnectivity = require("./slice_connectivity_schema").SliceConnectivity;

const Slice = new Schema({
    sliceId:String,
    resources:Schema.Types.Mixed,
    connectivities:{type:Map, of:SliceConnectivity},
    createdAt:Number
},{_id:false});

module.exports = {
    Slice:Slice
};