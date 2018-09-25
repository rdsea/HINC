const mongoose = require("mongoose");
const config = require("config");
const intopConfig = config.get("interoperability_service");
const uuidv4 = require('uuid/v4');
const MetadataModel = require("./interoperability_metadata/interoperability_metadata_model").Metadata;
const SliceInformation = require("./slice_information/slice_information_model").Slice;

console.log("in bridge_model:" + uuidv4());

mongoose.connect(intopConfig.MONGODB_URL);

const Schema = mongoose.Schema;
const ObjectId = Schema.ObjectId;

const Bridge = new Schema({
    id:ObjectId,
    slice: SliceInformation,
    metadata: MetadataModel,
    inputResourceId: String,
    outputResourceId: String
});


const MyBridge = mongoose.model("Bridge", Bridge);

module.exports = {
    saveNew:saveNew,
    update:update,
    disconnect:disconnect
};

function saveNew(){
    const instance = new MyBridge();
    instance.slice = {
        resources:{
            resource1:{
                test_resource:"test"
            },
            resource2:{
                akeia:"kawoe"
            }
        }
    };
    instance.metadata = {
        resource:"res",
        inputs:["i1","i2"]
    };
    return instance.save();
}

function update(){
    let query = MyBridge.find({});

    return query.exec().then((results)=>{
        let test = results[0];
        //test.slice = "update";
        test.metadata = {};
        return test.save();
    });
    //return new Promise(resolve => {resolve()});
}

function disconnect() {
    return mongoose.disconnect();
}