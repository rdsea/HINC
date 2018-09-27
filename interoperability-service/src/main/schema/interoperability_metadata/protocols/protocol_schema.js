const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const discriminatorKey = 'protocol_name';
const options = {_id:false, discriminatorKey: discriminatorKey};

const Protocol = new Schema({
    protocol_name:String
},options);

module.exports = {
    Protocol:Protocol,
    discriminatorKey:discriminatorKey
};


const MQTTProtocol = new Schema({
    qos:{type:Number, min:0, max:2}
},options);

//TODO define protocols and dataformats once subdocument inheritence is supported by mongoose:
//https://stackoverflow.com/questions/47674059/how-should-i-perform-subdocument-inheritance-in-mongoose