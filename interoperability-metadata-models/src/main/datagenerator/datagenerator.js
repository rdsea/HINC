const domain = require('./domain/metadata_domain');
const util = require('./util');
const basic = require('./domain/basic_types_values')

exports.randomResource = function(){
    let metadata = util.deepcopy(domain.metadata);

    initialize(metadata);

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


function initialize(object){
    if(object === null){
        return;
    }
    Object.keys(object).map(function (key) {
        exports.initAttribute(object, key, object[key]);
        });
}


exports.initAttribute = function(containingobject, attributename, attributevalue){
    let newvalue = "";

    if(typeof attributevalue === "string") {
        if(isBasicType(attributevalue)) {
            newvalue = getRandomBasicValue(attributevalue);
        }else{
            newvalue = attributevalue;
        }
    }else if(typeof attributevalue === "object"){
        if(Array.isArray(attributevalue)){
            if(attributevalue.length === 1){
                if(isBasicType(attributevalue[0])){
                    newvalue = [getRandomBasicValue(attributevalue[0])];
                }else{
                    initialize(attributevalue[0]);
                    newvalue = [attributevalue[0]];
                }
            }
        }else{
            // if domain
            if(attributevalue[basic.domain_values_identifier]) {
                attributevalue = getRandomArrayElement(attributevalue[basic.domain_values_identifier]);
                initialize(attributevalue);
                newvalue = attributevalue;
            }else {
                initialize(attributevalue);
                newvalue = attributevalue;
            }
        }
    }
    containingobject[attributename] = newvalue;
};

function isBasicType(attributeType){
    let basictypes = [basic.stringType, basic.decimalType, basic.integerType, basic.booleanType, basic.objectType];

    return basictypes.indexOf(attributeType)>-1;
}

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

function getRandomArrayElement(array){
    if(array.length === 0){
        return null;
    }

    let index = Math.floor(array.length * Math.random());
    return array[index];
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