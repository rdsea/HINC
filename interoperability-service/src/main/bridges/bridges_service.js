const mongoose = require("mongoose");
let config = require("config");
let intopConfig = config.get("interoperability_service");
const BridgeSchema = require("../schema/bridge_schema").InteroperabilityBridgeSchema;


const collectionName = intopConfig.BRIDGE_COLLECTION_NAME;
const BridgeModel = mongoose.model("Bridge", BridgeSchema, collectionName);

module.exports = {
    get:get,
    getList:getList,
    create:create,
    search:search,
    deleteBridge:deleteBridge,
    updateMetadata:updateMetadata
};

function get(id) {
    return BridgeModel.findById(id).exec();
}

function getList(limit){
    return BridgeModel.find({}).limit(limit).exec();
}

function create(bridge){
    return BridgeModel.create(bridge);
}

function search(query){
    return BridgeModel.find(query).exec();
}

function deleteBridge(id){
    return BridgeModel.findByIdAndDelete(id).exec();
}

function updateMetadata(id, metadata){
    let bridge;
    return BridgeModel.findById(id).exec().then((data)=>{
        bridge = data;
        bridge.metadata = metadata;
        // use mongodb directly to avoid usage of deprecated code by mongoose
        return mongoose.connection.db.collection(collectionName).findOneAndUpdate({_id:id},bridge);
    }).then(()=>{
        return new Promise((resolve => {resolve(bridge)}));
    })
}