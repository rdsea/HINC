// generates slice descriptions for the two different configurations that we require
// we assume that our system has been deployed according to the tool in the scenario folder

const fs = require("fs");
const execSync = require("child_process").execSync;
const http = require("http")

// CHANGE PARAMETERS HERE
LOCAL_COUNT = parseInt(process.env.LOCAL_COUNT);
PROVIDER_GROUP_COUNT = parseInt(process.env.PROVIDER_GROUP_COUNT);
GLOBAL_IP = process.env.GLOBAL_IP;
RESULT_ENDPOINT = process.env.RESULT_ENDPOINT;

// we generate the providerIds of each of the provider groups
let providerGroups = [];

http.get({host: "localhost", port: 3000})
for(let i=1;i<=LOCAL_COUNT;i++){
    let localId = `local${i}`;
    for(let j=0;j<PROVIDER_GROUP_COUNT;j++){
        providerGroups.push({
            sensor: `sensor${localId}set${j}`,
            mqtt: `cloudmqtt${localId}set${j}`,
            bigquery : `bigquery${localId}set${j}`,
            ingestion:  `ingestion${localId}set${j}`
        });
    }
}

let slices = [];

for(let i=0;i<providerGroups.length;i++){
    let template = require("./slice.json")
    let slice = JSON.parse(JSON.stringify(template));

    // the different slices only differ by the appropriate provider
    slice.resources.broker.providerUuid = providerGroups.mqtt;
    slice.resources.analytics.providerUuid = providerGroups.ingestion;
    slice.resources.datasink.providerUuid = providerGroups.bigquery;
    slice.resources.temp.providerUuid = providerGroups.sensor;
    slice.resources.hum.providerUuid = providerGroups.sensor;

    slices.push(slice);
}

// set the global management system URL
let out = execSync(`echo "http://${GLOBAL_IP}" | pizza config -s`);
console.log(out.toString())

// number of experiments
let count = 0;
function provisionSlice(slice){
    let timestamp = Date.now();
    count++
    slice.sliceId = `slice${timestamp}`
    fs.writeFileSync(`/tmp/slice${timestamp}.json`, JSON.stringify(slice, null, 4));

    let t = process.hrtime();
    execSync(`pizza slice create /tmp/slice${timestamp}.json`, {stdio:[0,1,2]});
    t = process.hrtime(t);  


    console.log(`execution took ${t[0]} seconds and ${t[1]} nanoseconds`)    
    // send request to result server
    let cmd = `curl -X GET "${RESULT_ENDPOINT}/result/${t[0]}/${t[1]}"`
    execSync(cmd);

    // delete the slice we just provisioned (to free up resources on the cluster)
    execSync(`pizza slice delete slice${timestamp}`, {stdio:[0,1,2]});
}


function start(){
    while(true){
        let index = Math.floor(Math.random()*slices.length)
        provisionSlice(slices[index]) 
    }
}

start()


