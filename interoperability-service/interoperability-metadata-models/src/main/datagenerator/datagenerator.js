const domain = require('./domain/metadata_domain');
const util = require('./util');
const basic = require('./domain/basic_types_values');
const prototype_d = require('./domain/prototype_domain');

exports.randomResource = function(){
    let metadata = util.deepcopy(domain.metadata);

    initialize(metadata);
    metadata.resource.category = prototype_d.prototypeCategory(metadata.resource.type.prototype);

    return metadata;
};


function initialize(object){
    if(object === null){
        return;
    }
    Object.keys(object).map(function (key) {
        exports.initAttribute(object, key, object[key]);
        });
}


exports.initAttribute = function(containingObject, attributeName, attributeValue){
    let newValue = "";

    if(typeof attributeValue === "string") {
        if(isBasicType(attributeValue)) {
            newValue = getRandomBasicValue(attributeValue);
        }else{
            newValue = attributeValue;
        }
    }else if(typeof attributeValue === "object"){
        if(Array.isArray(attributeValue)){
            if(attributeValue.length === 1){
                if(isBasicType(attributeValue[0])){
                    newValue = [getRandomBasicValue(attributeValue[0])];
                }else{
                    initialize(attributeValue[0]);
                    newValue = [attributeValue[0]];
                }
            }
        }else{
            if(attributeValue[basic.domain_values_identifier]) {
                attributeValue = getRandomArrayElement(attributeValue[basic.domain_values_identifier]);
            }

            initialize(attributeValue);
            newValue = attributeValue;

        }
    }
    containingObject[attributeName] = newValue;
};

function isBasicType(attributeType){
    let basictypes = [basic.stringType, basic.decimalType, basic.integerType, basic.booleanType, basic.objectType];

    return basictypes.indexOf(attributeType)>-1;
}

function getRandomBasicValue(attributeType){
    let randomValue;

    switch (attributeType){
        case basic.stringType: randomValue = "string_" + exports.randomString(); break;
        case basic.decimalType: randomValue = exports.randomDecimal(10); break;
        case basic.integerType: randomValue = exports.randomInteger(10); break;
        case basic.booleanType: randomValue = exports.randomBoolean(); break;
        case basic.objectType: randomValue = {}; break;
    }

    return randomValue;
}

function getRandomArrayElement(array){
    if(array.length === 0){
        return null;
    }

    let index = Math.floor(array.length * Math.random());
    return array[index];
}




exports.randomInteger = function(maxValue){
    let random = Math.random();

    return Math.round(random*maxValue);
};

exports.randomDecimal = function(maxValue){
    let random = Math.random();

    return random*maxValue;
};

exports.randomBoolean = function(){
    let random = Math.random();
    return random<0.5;
};

exports.randomString = function(){
    return Math.random().toString(36).substr(2);

};