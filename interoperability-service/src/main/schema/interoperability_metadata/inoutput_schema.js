const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const Protocol = require("./protocols/protocol_schema").Protocol;
const Dataformat = require("./dataformats/dataformat_schema").Dataformat;
const QoData = require("./quality/qodata_schema").QoData;
const QoService = require("./quality/qoservice_schema").QoService;


const InOutPut = new Schema({
    protocol:Schema.Types.Mixed,
    dataformat:Schema.Types.Mixed,
    qod:QoData,
    qos:QoService
},{_id:false});

module.exports = {
    Input:InOutPut,
    Output: InOutPut
};

//TODO define protocols and dataformats once subdocument inheritence is supported by mongoose:
//https://stackoverflow.com/questions/47674059/how-should-i-perform-subdocument-inheritance-in-mongoose