const express = require("express");
const app = express();
const swaggerUi = require('swagger-ui-express');
const YAML = require('yamljs');
const swaggerDocument = YAML.load('./src/main/openapi/openapi3.yaml');
const router = require("./router");
const bodyParser = require('body-parser');
const mongoose = require("mongoose");
let configModule = require('config');
let config = configModule.get('interoperability_service');
const publicIp = require('public-ip');
const PORT = config.SERVER_PORT;



mongoose.connect(config.MONGODB_URL, { useNewUrlParser: true });
app.use(bodyParser.json({ limit: '50mb' }));
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));
app.use('/', router);


//app.use(bodyParser.json());

//app.use(bodyParser.urlencoded({ limit: '50mb', extended: true, parameterLimit: 50000 }));

app.listen(PORT);
//TODO revert
console.log(`Running on http://localhost:${PORT}/api-docs`);
/*publicIp.v4().then(ip => {
  console.log(`Running on http://${ip}:${PORT}/api-docs`);
});*/




process.on('SIGINT', function(){
    mongoose.connection.close(function(){
        console.log("Mongoose default connection is disconnected due to application termination");
        process.exit(0);
    });
});