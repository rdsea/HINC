const express = require('express');
let router = express.Router({mergeParams: true});
const bodyParser = require('body-parser');
const performance_suite = require("../../test/performance/performance_suite");


router.use(bodyParser.json());

router.post('/', function (req, res) {
    performance_suite.startSuite();
    res.send("suite started");

});

module.exports.getRouter = function(){
    return router;
};