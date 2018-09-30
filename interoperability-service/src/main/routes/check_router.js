const express = require('express');
let router = express.Router({mergeParams: true});
const check = require("../check/intop_check");
const bodyParser = require('body-parser');


router.use(bodyParser.json());


router.post('/',  function (req, res) {
    let slice = req.body;
    if(req.body.slice){
        slice =req.body.slice;
    }
    let response = check.checkSlice(slice);
    res.send(response);
});

router.post('/contract',  function (req, res) {
    let slice = req.body.slice;
    let contract = req.body.contract;

    let response = check.checkWithContract(slice, contract);
    res.send(response);
});

module.exports.getRouter = function(){
    return router;
};