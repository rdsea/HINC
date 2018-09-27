const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const DataRights = require("./data_rights_schema").DataRights;
const Pricing = require("./pricing_schema").Pricing;
const Regulation = require("./regulation_schema").Regulation;


const DataContract = new Schema({
    data_rights:DataRights,
    pricing:Pricing,
    regulation:Regulation
},{_id:false});

module.exports = {
    DataContract:DataContract
};