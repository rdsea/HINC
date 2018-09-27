const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const discriminatorKey = 'dataformat_name';
const options = {_id:false, discriminatorKey: discriminatorKey};

const Dataformat = new Schema({
    dataformat_name:String
},options);

module.exports = {
    Dataformat:Dataformat,
    discriminatorKey:discriminatorKey
};



//TODO define dataformats once subdocument inheritence is supported by mongoose:
//https://stackoverflow.com/questions/47674059/how-should-i-perform-subdocument-inheritance-in-mongoose