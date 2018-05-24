const datagenerator = require('../main/datagenerator/datagenerator');
const assert = require('assert');


describe('metadata generator', function(){
    describe('todo: name', function(){
        it('todo: name', function(){
            let metadata = datagenerator.randomResource();

            assert.equal(metadata.resource.category, "iot");
        });
        it('random boolean', function(){
            let randomBooleans = [];
            let randomIntegers = [];
            let randomDecimals = [];
            let randomStrings = [];

            for(let i = 0; i<10; i++){
                randomBooleans.push(datagenerator.randomBoolean());
                randomIntegers.push(datagenerator.randomInteger(10))
                randomDecimals.push(datagenerator.randomDecimal(10));
                randomStrings.push(datagenerator.randomString() + " ");
            }

            console.log("Booleans: " + randomBooleans);
            console.log("Integers: " + randomIntegers);
            console.log("Decimals: " + randomDecimals);
            console.log("Strings: " + randomStrings);
        });
    });
});