const datagenerator = require('../main/datagenerator/datagenerator');
const assert = require('assert');


describe('metadata generator', function(){
    describe('todo: name', function(){
        it('todo: name', function(){
            let metadata = datagenerator.randomResource();

            assert.equal(metadata.resource.category, "iot");
        });
    });
});