const assert = require('assert');

const configModule = require('config');
const config = configModule.get('interoperability_service');



describe('mongoose model tests', function(){
    const service = require("../main/bridges/bridges_service");

    before(()=>{
        _populateDB(service);
    });

    after(()=>{
       service.disconnect();
    });

    it('test run', function () {
        return service.create(_examplebridge()).then((data)=>{
            console.log(data);
        }).then(()=>{
            return service.getList(2);
        }).then((data)=>{
            console.log(data);
        });
    });
    it('test run', function () {
        let id;
        return service.create(_examplebridge()).then((data)=>{
            console.log(data);
            id = data._id;
        }).then(()=>{
            return service.deleteBridge(id);
        }).then((data)=>{
            console.log("deleted :" + data);
        }).then((data)=>{
            return service.get(id);
        }).then((data)=>{
              console.log(data);
        });
    });
    it('test run', function () {
        return service.search({"slice.sliceId":"bridge 2"}).then((data)=>{
            console.log(data);
        });
    });
    it('test run', function () {
        let id;
        let metadata ={
            resource:{
                category:"network_function"
            },
            inputs:[],
            outputs:[]
        };
        return service.create(_examplebridge()).then((data)=>{
            console.log(data);
            id = data._id;
        }).then(()=>{
            return service.updateMetadata(id,metadata);
        }).then((data)=>{
            console.log("updated to:" + data);
        });
    });
});

function _populateDB(service) {
    for(let i=0;i<3;i++){
        let bridge = _examplebridge();
        bridge.slice.sliceId = `bridge ${i+1}`;
        service.create(bridge);
    }
}


function _examplebridge(){
    return {
        slice: {
            sliceId:"sliceId",
            resources:{resource1:"r1"},
            connectivities:{},
            createdAt:123
        },
        metadata: {
            resource:{
                category:"iot"
            },
            inputs:[],
            outputs:[]
        },
        inputResourceId: "inputResourceId",
        outputResourceId: "outputResourceId"
    };
}