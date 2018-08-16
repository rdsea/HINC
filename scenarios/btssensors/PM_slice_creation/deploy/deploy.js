// deploys a set number of users 

const fs = require("fs");
const execSync = require("child_process").execSync

// ADJUST PARAMETERS HERE
const USER_NB = 1
const LOCAL_COUNT = 1
const PROVIDER_GROUP_COUNT = 1
const GLOBAL_IP = "localhost:3000"
const RESULT_ENDPOINT = "http://localhost:3000"

// the default values in these containers are fine for now, use in the future
// const MONGODB_URL = "connection string"
// const COLLECTION = "collection to put test results"

let userclientDeploy = require("./userclient.json");
let masterDeploy = require("./master.json");
let masterService = require("./masterservice.json");

// set the number of user clients
userclientDeploy.spec.replicas = USER_NB;
// set environment variables
userclientDeploy.spec.containers[0].env.push({name: "LOCAL_COUNT", value: LOCAL_COUNT})
userclientDeploy.spec.containers[0].env.push({name: "PROVIDER_GROUP_COUNT", value: PROVIDER_GROUP_COUNT})
userclientDeploy.spec.containers[0].env.push({name: "GLOBAL_IP", value: GLOBAL_IP})
userclientDeploy.spec.containers[0].env.push({name: "RESULT_ENDPOINT", value: RESULT_ENDPOINT})


// write files
let timestamp = Date.now();
fs.writeFileSync(`/tmp/userclient${timestamp}.json`, JSON.stringify(userclientDeploy, null, 4));
fs.writeFileSync(`/tmp/master${timestamp}.json`, JSON.stringify(masterDeploy, null, 4));
fs.writeFileSync(`/tmp/masterservice${timestamp}.json`, JSON.stringify(masterService, null, 4));

// deploy master and user clients
let out = exec(`kubectl create -f /tmp/master${timestamp}.json`);
console.log(out.toString());

let out = exec(`kubectl create -f /tmp/masterservice${timestamp}.json`);
console.log(out.toString());

let out = exec(`kubectl create -f /tmp/userclient${timestamp}.json`);
console.log(out.toString());


