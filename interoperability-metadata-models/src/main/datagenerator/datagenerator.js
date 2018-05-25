const domain = require('./domain/metadata_domain');
const util = require('./util');
const basic = require('./domain/basic_types_values')

exports.randomResource = function(){
    let metadata = util.deepcopy(domain.metadata);

    //metadata.resource.category = basic.categoryValues[0];
    let attributes = getAttributes(metadata.resource);
    exports.initAttribute(attributes[0].containingobject,
        attributes[0].attributename,
        attributes[0].attributevalue);
    //initAttribute(metadata.resource, "category", metadata.resource.category);

    return metadata;
};

function getAttributes(object) {
    return Object.keys(object).map(function (key) {
        return {
            containingobject: object,
            attributename: key,
            attributevalue: object[key]
        };
    });
}


function initialize(metadata){

}


exports.initAttribute = function(containingobject, attributename, attributevalue){
    //let value = attribute;
    let newvalue = "";

    if(typeof attributevalue === "string") {
        newvalue = getRandomBasicValue(attributevalue);
    }else if(typeof attributevalue === "object"){
        if(Array.isArray(attributevalue)){
            if(attributevalue.length === 1 && getRandomBasicValue(attributevalue)){
                newvalue = getRandomBasicValue(attributevalue);
            }

        }else{
            //object
        }
    }
    containingobject[attributename] = newvalue;
};

function getRandomBasicValue(attributeType){
    let randomvalue;

    switch (attributeType){
        case basic.stringType: randomvalue = "string" + exports.randomString(); break;
        case basic.decimalType: randomvalue = exports.randomDecimal(10); break;
        case basic.integerType: randomvalue = exports.randomInteger(10); break;
        case basic.booleanType: randomvalue = exports.randomBoolean(); break;
        case basic.objectType: randomvalue = {}; break;
    }

    return randomvalue;
}






exports.randomInteger = function(maxvalue){
    let random = Math.random();

    return Math.round(random*maxvalue);
};

exports.randomDecimal = function(maxvalue){
    let random = Math.random();

    return random*maxvalue;
};

exports.randomBoolean = function(){
    let random = Math.random();
    return random<0.5;
};

exports.randomString = function(){
    return Math.random().toString(36).substr(2);

};