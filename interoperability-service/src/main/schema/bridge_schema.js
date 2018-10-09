const mongoose = require("mongoose");
const MetadataModel = require("./interoperability_metadata/interoperability_metadata_schema").Metadata;
const SliceInformation = require("./slice_information/slice_information_schema").Slice;


const Schema = mongoose.Schema;
const ObjectId = Schema.ObjectId;

const Bridge = new Schema({
    id:ObjectId,
    name:String,
    slice: SliceInformation,
    metadata: MetadataModel,
    inputResourceId: String,
    outputResourceId: String
},{
    toObject: {
        transform: function (doc, ret, game) {
            delete ret.__v;
            ret.id = ret._id;
            delete ret._id;
        }
    }
});



module.exports = {
    InteroperabilityBridgeSchema:Bridge
};
