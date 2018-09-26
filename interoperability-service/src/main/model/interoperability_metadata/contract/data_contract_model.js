const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const DataRights = require("./data_rights_model").DataRights;
const Pricing = require("./pricing_model").Pricing;
const Regulation = require("./regulation_model").Regulation;


const DataContract = new Schema({
    data_rights:DataRights,
    pricing:Pricing,
    regulation:Regulation
},{_id:false});

module.exports = {
    DataContract:DataContract
};