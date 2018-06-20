const express = require('express');
let router = express.Router();
const check = require("./check/intop_check");
const recommendation = require("./recommendation/intop_recommendation");
const bodyParser = require('body-parser');


router.use(bodyParser.json());

// define the home page route
router.post('/recommendation', function (req, res) {
    let slice = req.body.slice;
    let response = recommendation.getRecommendationsWithoutCheck(slice, "todo");
    res.send(response);
});

// define the about route
router.post('/check',  function (req, res) {
    let slice = req.body.slice;
    let response = check.checkSlice(slice);
    res.send(response);
});

module.exports = router;