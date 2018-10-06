const resource_check = require('../adaptors/resources');
const assert = require('assert');


describe('test_resource.getResource', function(){
    describe('0 - basic get resource information', function(){
        it('0_0_get_resource: get resource list', function () {
            let result = resource_check.getItems();
            console.log(result);
        });
    });

});
