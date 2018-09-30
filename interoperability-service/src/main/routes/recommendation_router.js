const express = require('express');
let router = express.Router({mergeParams: true});
const recommendation = require("../recommendation/intop_recommendation");
const bodyParser = require('body-parser');


router.use(bodyParser.json());

router.post('/', function (req, res) {
    let slice = req.body;
    if(req.body.slice){
        slice =req.body.slice;
    }
    recommendation.getRecommendations(slice).then(function (result) {
        res.send(result);
    })
});

router.post('/contract', function (req, res) {
    let slice = req.body.slice;
    let contract = req.body.contract;
    recommendation.getContractRecommendations(slice, contract).then(function (result) {
        res.send(result);
    })
});

module.exports.getRouter = function(){
    return router;
};