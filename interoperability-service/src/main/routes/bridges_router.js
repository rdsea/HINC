const express = require('express');
let router = express.Router({mergeParams: true});
const bodyParser = require('body-parser');
const service = require("../bridges/bridges_service");


router.use(bodyParser.json());

router.get("/", function (req, res) {
    let limit = req.query.limit;
    let bridges = service.getList(limit);
    res.send(bridges);
});

router.get("/:id", function (req, res) {
    let id = req.params.id;
    let bridge = service.get(id);
    res.send(bridge);
});

router.put("/", function (req, res) {
    let toCreate = req.body.interoperabilityBridge;
    let created = service.create(toCreate);
    res.send(created);
});

router.get("/search", function (req, res) {
    let query = req.query.query;
    let bridges = service.search(query);
    res.send(bridges);
});

router.post("/search", function (req, res) {
    let query = req.body.query;
    let bridges = service.search(query);
    res.send(bridges);
});

router.delete("/{id}", function (req, res) {
    let id = req.params.id;
    let deleted = service.deleteBridge(id);
    res.send(deleted);
});

router.put("/{id}/metadata", function (req, res) {
    let id = req.params.id;
    let metadata = req.body.metadata;
    let updatedBridge = service.updateMetadata(id, metadata);
    res.send(updatedBridge);
});

module.exports.getRouter = function(){
    return router;
};