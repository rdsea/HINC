const express = require('express');
let router = express.Router({mergeParams: true});
const bodyParser = require('body-parser');

const bridgeRouter = require("./routes/bridges_router").getRouter();
const recommendationRouter = require("./routes/recommendation_router").getRouter();
const checkRouter = require("./routes/check_router").getRouter();

router.use(bodyParser.json());

router.use('/interoperability/bridges', bridgeRouter);
router.use('/interoperability/recommendation', recommendationRouter);
router.use('/interoperability/check', checkRouter);

module.exports = router;