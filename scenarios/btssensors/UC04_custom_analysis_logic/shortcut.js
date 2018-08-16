// configures the nodered flow with the broker IP of the slice.
const fs = require("fs");
let slice = require("./slice.json")

console.log(`slice ${slice.sliceId} imported!`);

let broker = slice.resources.broker;

let host = broker.parameters.ingressAccessPoints[0].host 
let port = broker.parameters.ingressAccessPoints[0].port

let noderedFlow = require("./nodered_flow.json");

for(let i=0;i<noderedFlow.length;i++){
    if(noderedFlow[i].type==="mqtt-broker"){
        console.log(`configuring nodered broker with ${host}:${port}`);
        noderedFlow[i].broker = host;
        noderedFlow[i].port = port;
    }
}
// write changes
fs.writeFileSync("./nodered_flow.json", JSON.stringify(noderedFlow, null, 4));

// changes fields in the analysis client to consume from the new topic
slice.resources.analytics.parameters.egressAccessPoints[0].topics = ["uc4topic_custom"];
slice.resources.analytics.parameters.bigQuery.tables[0].topics = ["uc4topic_custom"];
//write changes
fs.writeFileSync("./slice.json", JSON.stringify(slice, null, 4));