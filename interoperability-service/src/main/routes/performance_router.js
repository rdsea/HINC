const express = require('express');
let router = express.Router({mergeParams: true});
const bodyParser = require('body-parser');
const performance_suite = require("../../test/performance/performance_suite");


router.use(bodyParser.json());

router.post('/', function (req, res) {
    let parameterstring = `parameter:
    node_counts: \t${req.body.node_counts}
    metadata_counts: \t${req.body.metadata_counts}
    iterations: \t${req.body.num}
    instance_name: \t${req.body.instance_name}`;


    console.info(`start tests\n${parameterstring}`)
    performance_suite.startSuite(req.body.node_counts, req.body.metadata_counts, req.body.num, req.body.instance_name);
    res.send(`suite started\n${parameterstring}`);

});

module.exports.getRouter = function(){
    return router;
};