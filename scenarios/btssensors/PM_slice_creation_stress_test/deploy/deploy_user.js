// deploys a set number of users 

const fs = require("fs");
const execSync = require("child_process").execSync

// ADJUST PARAMETERS HERE
const USER_NB = 5
const LOCAL_COUNT = 1
const PROVIDER_GROUP_COUNT = 1
const GLOBAL_IP = "global"
const RESULT_ENDPOINT = "http://testmaster:3000"

let userclientDeploy = require("./userclient.json");

// set the number of user clients
userclientDeploy.spec.replicas = USER_NB;
// set environment variables
userclientDeploy.spec.template.spec.containers[0].env.push({name: "LOCAL_COUNT", value: LOCAL_COUNT+""})
userclientDeploy.spec.template.spec.containers[0].env.push({name: "PROVIDER_GROUP_COUNT", value: PROVIDER_GROUP_COUNT+""})
userclientDeploy.spec.template.spec.containers[0].env.push({name: "GLOBAL_IP", value: GLOBAL_IP})
userclientDeploy.spec.template.spec.containers[0].env.push({name: "RESULT_ENDPOINT", value: RESULT_ENDPOINT})



// write files
let timestamp = Date.now();
fs.writeFileSync(`/tmp/userclient${timestamp}.json`, JSON.stringify(userclientDeploy, null, 4));

out = execSync(`kubectl create -f /tmp/userclient${timestamp}.json`);
console.log(out.toString());


