const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const discriminatorKey = 'prototype';
const options = {_id:false, discriminatorKey: discriminatorKey};

const Prototype = new Schema({
    prototype:String
},options);

module.exports = {
    Prototype:Prototype,
    discriminatorKey:discriminatorKey
};



//TODO define prototypes once subdocument inheritence is supported by mongoose:
//https://stackoverflow.com/questions/47674059/how-should-i-perform-subdocument-inheritance-in-mongoose