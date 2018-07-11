const express = require('express');
let router = express.Router();
const check = require("./check/intop_check");
const recommendation = require("./recommendation/intop_recommendation");
const bodyParser = require('body-parser');


router.use(bodyParser.json());

router.post('/recommendation', function (req, res) {
    let slice = req.body;
    if(req.body.slice){
        slice =req.body.slice;
    }
    recommendation.getRecommendations(slice).then(function (result) {
        res.send(result);
    })
});

router.post('/recommendation/contract', function (req, res) {
    let slice = req.body.slice;
    let contract = req.body.contract;
    recommendation.getContractRecommendations(slice, contract).then(function (result) {
        res.send(result);
    })
});

router.post('/check',  function (req, res) {
    let slice = req.body;
    if(req.body.slice){
        slice =req.body.slice;
    }
    let response = check.checkSlice(slice);
    res.send(response);
});

router.post('/check/contract',  function (req, res) {
    let slice = req.body.slice;
    let contract = req.body.contract;

    let response = check.checkWithContract(slice, contract);
    res.send(response);
});


module.exports = router;