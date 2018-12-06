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
    let starttime = process.hrtime();
    recommendation.getRecommendations(slice).then(function (result) {
        let diff = process.hrtime(starttime);
        result.time = diff;
        res.send(result);
    })
});

router.post('/contract', function (req, res) {
    let slice = req.body.slice;
    let contract = req.body.contract;

    let starttime = process.hrtime();
    recommendation.getContractRecommendations(slice, contract).then(function (result) {
        let diff = process.hrtime(starttime);
        result.time = diff;
        res.send(result);
    })
});

module.exports.getRouter = function(){
    return router;
};