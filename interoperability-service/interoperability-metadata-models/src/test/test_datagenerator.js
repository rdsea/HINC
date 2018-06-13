const datagenerator = require('../main/datagenerator/datagenerator');
const assert = require('assert');
const basic = require('../main/datagenerator/domain/basic_types_values');
const util = require('../main/datagenerator/util');


describe('metadata generator', function(){
    describe('test generate resource',function () {
        it('test generate randomResource', function () {
            let metadata = datagenerator.randomResource();

            assert.equal(Array.isArray(metadata.inputs), true);
            assert.equal(Array.isArray(metadata.outputs), true);

        });
    });
    describe('test initAttribute', function(){
        it('init basic.stringType', function(){
            let object = {stringValue:basic.stringType};
            datagenerator.initAttribute(object, "stringValue", object.stringValue);
            assert.equal(typeof object.stringValue, "string");
            assert.notEqual(object.stringValue, basic.stringType);

        });
        it('init basic.decimalType', function(){
            let object = {decimalValue:basic.decimalType};
            datagenerator.initAttribute(object, "decimalValue", object.decimalValue);
            assert.equal(typeof object.decimalValue, "number");
            assert.equal(object.decimalValue%1!==0, true);

        });
        it('init basic.integerType', function(){
            let object = {integerValue:basic.integerType};
            datagenerator.initAttribute(object, "integerValue", object.integerValue);
            assert.equal(typeof object.integerValue, "number");
            assert.equal(object.integerValue%1===0, true);

        });
        it('init basic.booleanType', function(){
            let object = {booleanValue:basic.booleanType};
            datagenerator.initAttribute(object, "booleanValue", object.booleanValue);
            assert.equal(typeof object.booleanValue, "boolean");

        });
        it('init list of basic.booleanType', function(){
            let object = {listValue:[basic.booleanType]};
            datagenerator.initAttribute(object, "listValue", object.listValue);

            assert.equal(Array.isArray(object.listValue), true);
            assert.equal(object.listValue.length, 1);
            assert.equal(typeof object.listValue[0], "boolean");
        });
        it('init list of objectdefinition', function(){
            let objectdefinition = {booleanValue:basic.booleanType, numberValue:basic.integerType, stringValue:basic.stringType};
            let object = {listValue:[objectdefinition]};
            datagenerator.initAttribute(object, "listValue", object.listValue);

            assert.equal(Array.isArray(object.listValue), true);
            assert.equal(object.listValue.length, 1);
            assert.equal(typeof object.listValue[0].booleanValue, "boolean");
            assert.equal(typeof object.listValue[0].stringValue, "string");
            assert.equal(typeof object.listValue[0].numberValue, "number");
        });
        it('init attribute of valueDomain', function(){
            let values = ["value1", "value2"];
            let valuedomain = util.createValueDomain(values);
            let object = {value:valuedomain};
            datagenerator.initAttribute(object, "value", object.value);

            assert.equal(values.indexOf(object.value)>-1, true);
        });
        it('init attribute of empty valueDomain', function(){
            let values = [];
            let valuedomain = util.createValueDomain(values);
            let object = {value:valuedomain};
            datagenerator.initAttribute(object, "value", object.value);
            assert.equal(object.value, null);
        });
        it('init attribute of basic.objectType', function(){
            let object = {value:basic.objectType};
            datagenerator.initAttribute(object, "value", object.value);

            assert.deepEqual(object.value, {});
        });
        it('init attribute of objectdefinition', function(){
            let objectdefinition = {booleanValue:basic.booleanType, numberValue:basic.integerType, stringValue:basic.stringType};
            let object = {value:objectdefinition};
            datagenerator.initAttribute(object, "value", object.value);

            assert.equal(typeof object.value.booleanValue, "boolean");
            assert.equal(typeof object.value.stringValue, "string");
            assert.equal(typeof object.value.numberValue, "number");
        });
        it('init attribute of empty objectdefinition', function(){
            let objectdefinition = {};
            let object = {value:objectdefinition};
            datagenerator.initAttribute(object, "value", object.value);
            assert.deepEqual(object.value, {});
        });

    });
    describe('random basic data generators', function(){
        it('random Booleans', function(){
            let randomBooleans = [];

            for(let i = 0; i<10; i++){
                randomBooleans.push(datagenerator.randomBoolean());
            }
            console.log("Booleans: " + randomBooleans);
        });
        it('random Integers', function(){
            let randomIntegers = [];

            for(let i = 0; i<10; i++){

                randomIntegers.push(datagenerator.randomInteger(10));
            }

            console.log("Integers: " + randomIntegers);
        });
        it('random Decimals', function(){
            let randomDecimals = [];

            for(let i = 0; i<10; i++){
                randomDecimals.push(datagenerator.randomDecimal(10));
            }

            console.log("Decimals: " + randomDecimals);
        });
        it('random Strings', function(){
            let randomStrings = [];

            for(let i = 0; i<10; i++){
                randomStrings.push(datagenerator.randomString() + " ");
            }

            console.log("Strings: " + randomStrings);
        });
    });
});