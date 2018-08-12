const parse = require("url-parse")

x = "mqtt://kcybqxap:srGSeyBPGbPZ@m23.cloudmqtt.com:14310";
y = parse(x);
console.log(JSON.stringify(y, null, 4))