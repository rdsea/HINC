const express = require('express');
let router = express.Router({mergeParams: true});
const bodyParser = require('body-parser');


router.use(bodyParser.json());

router.get("/", function (req, res) {
    res.send("GET /bridge/")
});

module.exports.getRouter = function(){
    return router;
};