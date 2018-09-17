const express = require("express");
const app = express();
const swaggerUi = require('swagger-ui-express');
const YAML = require('yamljs');
const swaggerDocument = YAML.load('./src/main/openapi/openapi3.yaml');
const router = require("./router");
const bodyParser = require('body-parser');
const config = require('../config');
const publicIp = require('public-ip');
const PORT = config.SERVER_PORT;



app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));
app.use('/interoperability', router);
app.use(bodyParser.json());

app.listen(PORT);
publicIp.v4().then(ip => {
  console.log(`Running on http://${ip}:${PORT}/api-docs`);
});
