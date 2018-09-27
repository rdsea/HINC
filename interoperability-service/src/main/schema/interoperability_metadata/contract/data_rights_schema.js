const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const DataRights = new Schema({
    derivation: Boolean,
    collection_rights: Boolean,
    reproduction: Boolean,
    commercial_usage: Boolean,
    attribution: Boolean
}, {_id: false});

module.exports = {
    DataRights: DataRights
};