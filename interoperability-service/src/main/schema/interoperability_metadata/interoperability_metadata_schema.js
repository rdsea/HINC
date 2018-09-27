const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const inoutput = require("./inoutput_schema");
const ResourceMetadata = require("./resource_metadata_schema").ResourceMetadata;

const Metadata = new Schema({
    resource:ResourceMetadata,
    //inputs:[inoutput.Input],
    //outputs:[inoutput.Output]
},{_id:false});

module.exports = {
    Metadata:Metadata
};