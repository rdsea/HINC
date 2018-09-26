const mongoose = require("mongoose");
const config = require("config");
const intopConfig = config.get("interoperability_service");
const BridgeModel = require("../model/bridge_model").InteroperabilityBridge;

module.exports = {
    get:get,
    getList:getList,
    create:create,
    search:search,
    deleteBridge:deleteBridge,
    updateMetadata:updateMetadata,
    disconnect:disconnect
};

mongoose.connect(intopConfig.MONGODB_URL, { useNewUrlParser: true });

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
        return mongoose.connection.db.collection("bridges").findOneAndUpdate({_id:id},bridge);
    }).then(()=>{
        return new Promise((resolve => {resolve(bridge)}));
    })
}

function disconnect() {
    return mongoose.disconnect();
}

process.on('SIGINT', function(){
    mongoose.connection.close(function(){
        console.log("Mongoose default connection is disconnected due to application termination");
        process.exit(0);
    });
});