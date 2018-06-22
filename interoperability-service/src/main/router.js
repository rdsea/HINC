const express = require('express');
let router = express.Router();
const check = require("./check/intop_check");
const recommendation = require("./recommendation/intop_recommendation");
const bodyParser = require('body-parser');


router.use(bodyParser.json());

// define the home page route
router.post('/recommendation', function (req, res) {
    let slice = req.body;
    if(req.body.slice){
        slice =req.body.slice;
    }
    recommendation.applyRecommendations(slice).then(function (result) {
        res.send(result);
    })
});

// define the about route
router.post('/check',  function (req, res) {
    let slice = req.body;
    if(req.body.slice){
        slice =req.body.slice;
    }
    let response = check.checkSlice(slice);
    res.send(response);
});

module.exports = router;