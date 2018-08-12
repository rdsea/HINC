const fs = require("fs");

let slice = require("./slice.json");
let firewall = require("./firewall.json").firewall;

// we apply the firewall to the analytics client
let analysisResourceUuid = slice.resources.analytics.uuid
firewall.parameters.serviceName = analysisResourceUuid;

// the firewall resource by default denies all communication
// we need to open communication with the beroker manually
let brokeResourceUuid = slice.resources.broker.uuid
firewall.parameters.ingress.services.push(brokeResourceUuid);
firewall.parameters.egress.services.push(brokeResourceUuid);
firewall.parameters.ingress.ports.push(1883)
firewall.parameters.egress.ports.push(1883);

slice.resources.firewall = firewall;

// write changes to the slice
fs.writeFileSync("./slice.json", JSON.stringify(slice, null, 4));
