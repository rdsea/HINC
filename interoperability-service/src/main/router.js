const express = require('express');
let router = express.Router({mergeParams: true});
const bodyParser = require('body-parser');

const bridgeRouter = require("./routes/bridge_router").getRouter();
const recommendationRouter = require("./routes/recommendation_router").getRouter();
const checkRouter = require("./routes/check_router").getRouter();

router.use(bodyParser.json());

router.use('/bridge', bridgeRouter);
router.use('/recommendation', recommendationRouter);
router.use('/check', checkRouter);



module.exports = router;