const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const DataContract = require("./contract/data_contract_schema").DataContract;
const QoData = require("./quality/qodata_schema").QoData;
const QoService = require("./quality/qoservice_schema").QoService;

const ResourceMetadata = new Schema({
    category:{type:String, enum:["iot_resource", "network_function_service", "cloud_service", "iot", "network_function", "cloud", "intop_bridge"]},
    type:Schema.Types.Mixed,
    data_contract:DataContract,
    qod:QoData,
    qos:QoService
},{_id:false});

module.exports = {
    ResourceMetadata:ResourceMetadata
};