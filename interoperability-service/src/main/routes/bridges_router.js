const express = require('express');
let router = express.Router({mergeParams: true});
const bodyParser = require('body-parser');
const service = require("../bridges/bridges_service");


router.use(bodyParser.json());

router.get("/", function (req, res) {
    let limit = req.query.limit;
    service.getList(limit).then((bridges)=>{
        res.send(bridges);
    });
});

router.get("/:id", function (req, res) {
    let id = req.params.id;
    service.get(id).then((bridge)=>{
        res.send(bridge);
    });
});

router.put("/", function (req, res) {
    let toCreate = req.body.bridge;
    service.create(toCreate).then((created)=>{
        res.send(created);
    });
});

router.post("/search", function (req, res) {
    let query = req.body;
    service.search(query).then((result)=>{
        res.send(result);
    });
});

router.delete("/:id", function (req, res) {
    let id = req.params.id;
    service.deleteBridge(id).then((result)=>{
        res.send(result);
    });
});

router.put("/:id/metadata", function (req, res) {
    let id = req.params.id;
    let metadata = req.body.metadata;
    service.updateMetadata(id, metadata).then((result)=>{
        res.send(result);
    });
});

module.exports.getRouter = function(){
    return router;
};