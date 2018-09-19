const axios = require('axios');
const amqpTools = require('./amqpTools');
const assert = require('assert');
assert.notEqual(process.env.CLOUDAMQP_KEY,null);
assert.notEqual(process.env.CLOUDAMQP_KEY,'');
amqpTools.init();
