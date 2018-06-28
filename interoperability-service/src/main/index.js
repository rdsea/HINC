const express = require("express");
const app = express();
const swaggerUi = require('swagger-ui-express');
const YAML = require('yamljs');
const swaggerDocument = YAML.load('./main/swagger.yaml');
const router = require("./router");
const bodyParser = require('body-parser');

const PORT = 8081;
const HOST = '0.0.0.0';



app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));
app.use('/interoperability', router);
app.use(bodyParser.json());

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);