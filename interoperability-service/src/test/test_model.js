const assert = require('assert');

const configModule = require('config');
const config = configModule.get('interoperability_service');
const testconfig = configModule.get('test');

const mongoose = require("mongoose");
mongoose.connect(config.MONGODB_URL, { useNewUrlParser: true });


/* test code commented because it changes the state of the database*/

describe('mongoose model tests', function(){
    const service = require("../main/bridges/bridges_service");

    before(()=>{
        _populateDB(service);
    });

    after(()=>{
        if(process.env.NODE_ENV === "test" && testconfig.drop_bridge_collection){
            mongoose.connection.db.dropCollection(config.BRIDGE_COLLECTION_NAME);
        }
        mongoose.disconnect();
    });

    it('test create', function () {
        let old_count;
        return service.getList(0).then((data)=>{
                old_count = data.length;
                return service.create(_examplebridge());
            }).then((data)=>{
                console.log(data);
            }).then(()=>{
                return service.getList(0);
            }).then((data)=>{
                assert(data.length===old_count+1);
                console.log(data);
            });
    });
    it('test delete', function () {
        let id;
        return service.create(_examplebridge()).then((data)=>{
            console.log("created: " + data);
            id = data._id;
            assert(id!==null);
        }).then(()=>{
            return service.deleteBridge(id);
        }).then((data)=>{
            console.log("deleted :" + data);
        }).then((data)=>{
            return service.get(id);
        }).then((data)=>{
            assert(data===null);
        });
    });
    it('test search', function () {
        let bridge = _examplebridge();
        bridge.slice.sliceId = "bridge 2";

        return service.create(bridge).then(()=>{
                return service.search({"slice.sliceId":"bridge 2"})})
            .then((data)=>{
                assert(data.length===2);
            });
    });
    it('test update metadata', function () {
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
            return service.get(id);
        }).then((data) =>{
            assert(data.metadata.resource.category === "network_function");
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