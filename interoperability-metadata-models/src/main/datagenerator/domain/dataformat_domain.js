const basic = require('./basic_types_values');
const util = require('../util');

exports.dataformat = util.createValueDomain([apache_avro() ,binary() ,cbor() ,csv() ,json() ,plaintext() ,rdf() ,xml()]);

//********************************************************************************************************** dataformats

function apache_avro(){
    return {
        encoding:basic.stringType,
        dataformat_name:"apache_avro",
        schema:basic.objectType
    }
}
function binary(){
    return {
        encoding:basic.stringType,
        dataformat_name:"binary"
    }
}
function cbor(){
    return {
        encoding:basic.stringType,
        dataformat_name:"cbor"
    }
}
function csv(){
    return {
        encoding:basic.stringType,
        dataformat_name:"csv",
        seperator:basic.stringType,
        newline_seperator:basic.stringType,
        headers:basic.stringType
    }
}
function json(){
    return {
        encoding:basic.stringType,
        dataformat_name:"json"
    }
}
function plaintext(){
    return {
        encoding:basic.stringType,
        dataformat_name:"plaintext"
    }
}
function rdf(){
    return {
        encoding:basic.stringType,
        dataformat_name:"rdf"
    }
}
function xml(){
    return {
        encoding:basic.stringType,
        dataformat_name:"xml"
    }
}
