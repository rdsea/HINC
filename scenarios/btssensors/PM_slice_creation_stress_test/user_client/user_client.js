// generates slice descriptions for the two different configurations that we require
// we assume that our system has been deployed according to the tool in the scenario folder

let promisify = require("util").promisify;
let s = require("child_process").spawn;
let writeFile = promisify(require("fs").writeFile);
let http = require("http");
let exec = promisify(require("child_process").exec)

// a special promise based spawn that redirects std to the parent process
function spawn(cmd) {
    return new Promise((resolve, reject) => {
        let tokens = cmd.split(" ");
        let command = tokens[0];
        let args = tokens.splice(1, tokens.length-1);

        let childProcess = s(command, args, {stdio: "inherit"});
        childProcess.on('close', (code) => {
            if(code !==0){
                console.log(`comand ${cmd} exited with code ${code}!`);
                throw new Error(code);
            }

            resolve();
        });
    });
}

// // CHANGE PARAMETERS HERE
LOCAL_COUNT = parseInt(process.env.LOCAL_COUNT);
PROVIDER_GROUP_COUNT = parseInt(process.env.PROVIDER_GROUP_COUNT);
GLOBAL_IP = process.env.GLOBAL_IP;
RESULT_ENDPOINT = process.env.RESULT_ENDPOINT;

// we generate the providerIds of each of the provider groups
let providerGroups = [];

for(let i=1;i<=LOCAL_COUNT;i++){
    let localId = `local${i}`;
    for(let j=0;j<PROVIDER_GROUP_COUNT;j++){
        providerGroups.push({
            sensor: `sensor`,
            mqtt: `mqtt`,
            bigquery : `bigquery`,
            ingestion:  `ingest`
        });
    }
}

let slices = [];

for(let i=0;i<providerGroups.length;i++){
    let template = require("./slice.json")
    let slice = JSON.parse(JSON.stringify(template));

    // the different slices only differ by the appropriate provider
    slice.resources.broker.providerUuid = providerGroups[i].mqtt;
    slice.resources.analytics.providerUuid = providerGroups[i].ingestion;
    slice.resources.datasink.providerUuid = providerGroups[i].bigquery;
    slice.resources.temp.providerUuid = providerGroups[i].sensor;
    slice.resources.hum.providerUuid = providerGroups[i].sensor;

    let timestamp = Date.now();
    slice.resources.analytics.parameters.bigQuery.dataset = "dataset"+timestamp
    slice.resources.datasink.parameters.datasetId= "dataset"+timestamp

    slices.push(slice);
}


// number of experiments
function provisionSlice(slice){
    let timestamp = Date.now();
    slice.sliceId = `slice${timestamp}`
    let time = null;
    // write slice description
    return writeFile(`/tmp/slice${timestamp}.json`, JSON.stringify(slice, null, 4)).then((res) => {
        t = process.hrtime();
        // pizza provision slice
        return spawn(`pizza slice create /tmp/slice${timestamp}.json`);
    }).then(() => {
        t = process.hrtime(t);
        // send execution back to master
        console.log(`execution took ${t[0]} seconds and ${t[1]} nanoseconds`);
        return new Promise((resolve, reject) => {
            http.get(`${RESULT_ENDPOINT}/result/${t[0]}/${t[1]}`, (res) => {
                resolve();
            });
        });
    }).then(() => {
        return new Promise((resolve, reject) => {
            http.get(`${RESULT_ENDPOINT}/successful`, (res) => {
                resolve();
            })
        })
    }).catch((err) => {
        console.log(err);
    });
}


function start(){
    exec(`echo "http://${GLOBAL_IP}:8080" | pizza config -s`).then((res) => {
        console.log(res.stdout);
        return provisionSlice(slices[0])
    }).catch((err) => {
        console.log(err)
    })
}

start()


