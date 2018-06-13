const express = require('express');
let router = express.Router();
const check = require("./check/intop_check");
const recommendation = require("./recommendation/intop_recommendation");

// middleware that is specific to this router
router.use(function timeLog (req, res, next) {
    console.log('Time: ', Date.now())
    next()
});

// define the home page route
router.get('/recommendation', function (req, res) {
    res.send('Interoperability Recommendation')
});

// define the about route
router.get('/check', function (req, res) {
    res.send('Interoperability Check')
});

module.exports = router;