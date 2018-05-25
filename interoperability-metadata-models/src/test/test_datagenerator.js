const datagenerator = require('../main/datagenerator/datagenerator');
const assert = require('assert');
const basic = require('../main/datagenerator/domain/basic_types_values');


describe('metadata generator', function(){
    describe('test initAttribute', function(){
        it('init basic.stringType', function(){
            let object = {stringvalue:basic.stringType};
            datagenerator.initAttribute(object, "stringvalue", object.stringvalue);
            assert.equal(typeof object.stringvalue, "string");
            assert.notEqual(object.stringvalue, basic.stringType);

        });
        it('init basic.decimalType', function(){
            let object = {decimalvalue:basic.decimalType};
            datagenerator.initAttribute(object, "decimalvalue", object.decimalvalue);
            assert.equal(typeof object.decimalvalue, "number");
            assert.equal(object.decimalvalue%1!==0, true);

        });
        it('init basic.integerType', function(){
            let object = {integervalue:basic.integerType};
            datagenerator.initAttribute(object, "integervalue", object.integervalue);
            assert.equal(typeof object.integervalue, "number");
            assert.equal(object.integervalue%1===0, true);

        });
        it('init basic.booleanType', function(){
            let object = {booleanvalue:basic.booleanType};
            datagenerator.initAttribute(object, "booleanvalue", object.booleanvalue);
            assert.equal(typeof object.booleanvalue, "boolean");

        });
        it('init list of basic.booleanType', function(){
            let object = {listvalue:[basic.booleanType]};
            datagenerator.initAttribute(object, "listvalue", object.listvalue);

            assert.equal(Array.isArray(object.listvalue), true);
            assert.equal(object.listvalue.length, 1);
            assert.equal(typeof object.listvalue[0], "boolean");
        });
        it('init attribute of valueDomain', function(){
            let valuedomain = ["value1", "value2"];
            let object = {value:valuedomain};
            datagenerator.initAttribute(object, "value", object.value);

            assert.equal(valuedomain.indexOf(object.value)>-1, true);
        });
        it('init attribute of empty valueDomain', function(){
            let valuedomain = [];
            let object = {value:valuedomain};
            datagenerator.initAttribute(object, "value", object.value);
            assert.equal(object, null);
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
            assert.equal(typeof object.value.stringvalue, "string");
            assert.equal(typeof object.value.integervalue, "number");

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