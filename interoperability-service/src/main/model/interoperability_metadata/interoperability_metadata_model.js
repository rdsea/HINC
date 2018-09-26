const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const inoutput = require("./inoutput_model");
const ResourceMetadata = require("./resource_metadata_model").ResourceMetadata;

const Metadata = new Schema({
    resource:ResourceMetadata,
    //inputs:[inoutput.Input],
    //outputs:[inoutput.Output]
},{_id:false});

module.exports = {
    Metadata:Metadata
};