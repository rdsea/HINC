const assert = require('assert');

const configModule = require('config');
const config = configModule.get('interoperability_service');



describe('mongoose model tests', function(){
    it('test run', function () {
        const bridgeModel = require("../../src/main/model/bridge_model");
        return bridgeModel.saveNew().then(()=>{
            return bridgeModel.update();
        }).then(()=>{
            return bridgeModel.disconnect();
        })
    });
});