// deploys a set number of users 

const fs = require("fs");
const execSync = require("child_process").execSync


 const MONGODB_URL = "mongodb://ling-test-sandbox:27017/?maxPoolSize=2000"
// const COLLECTION = "collection to put test results"

let masterDeploy = require("./master.json");
let masterService = require("./masterservice.json");

// set environment variables

masterDeploy.spec.template.spec.containers[0].env.push({name: "MONGODB_URL", value: MONGODB_URL})


// write files
let timestamp = Date.now();
fs.writeFileSync(`/tmp/master${timestamp}.json`, JSON.stringify(masterDeploy, null, 4));
fs.writeFileSync(`/tmp/masterservice${timestamp}.json`, JSON.stringify(masterService, null, 4));

// deploy master and user clients
let out = execSync(`kubectl create -f /tmp/master${timestamp}.json`);
console.log(out.toString());

out = execSync(`kubectl create -f /tmp/masterservice${timestamp}.json`);
console.log(out.toString());
