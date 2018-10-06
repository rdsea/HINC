const provider_check = require('../adaptors/providers');
const assert = require('assert');


describe('test_provider.getProvider', function(){
    describe('0 - basic get provider information', function(){
        it('0_0_get_provider: get provider', function () {
            let result = provider_check.getProvider();
            console.log("Result of get provider");
            console.log(result);
        });
    });

});
