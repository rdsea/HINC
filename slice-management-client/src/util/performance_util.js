const fs = require("fs");

module.exports = {sortResults:sortResults,
                    nodeAxisCsv:nodeAxisCsv,
                    metadataAxisCsv:metadataAxisCsv,
                    toCsvFile:toCsvFile,
                    metadataSpreadsheetCsv:metadataSpreadsheetCsv,
                    nodesSpreadsheetCsv:nodesSpreadsheetCsv
                };

function sortResults(resultsarray, operations, testinstancenames, nodecounts, metadatacounts){
    let sortedresults = {};

    for(let o = 0; o<operations.length; o++) {
        sortedresults[operations[o]]={};
        for(let t = 0; t<testinstancenames.length; t++) {
            sortedresults[operations[o]][testinstancenames[t]]={}
            for (let n = 0; n < nodecounts.length; n++) {
                sortedresults[operations[o]][testinstancenames[t]]["node_" + nodecounts[n]] = {};
                for (let m = 0; m < metadatacounts.length; m++) {
                    sortedresults[operations[o]][testinstancenames[t]]["node_" + nodecounts[n]]["metadata_" + metadatacounts[m]]
                        = resultsarray.filter((result) => {
                        return result.operation === operations[o]
                            && result.instancename === testinstancenames[t]
                            && result.nodecount === nodecounts[n]
                            && result.metadatacount === metadatacounts[m]
                    }).map(r => r.time_ms);
                }
            }
        }
    }
    return JSON.parse(JSON.stringify(sortedresults));
}


function nodeAxisCsv(instance, nodecounts, metadatacounts){
    let nodes_header = ["metadata", "nodes"];
    let iterations = instance["node_" + nodecounts[0]]["metadata_" + metadatacounts[0]].length;
    for(let i = 0; i<iterations; i++){
        nodes_header.push("time_ms_" + (i+1));
    }
    let csv = [];
    csv[0] = nodes_header;
    let i = 1;
    for (let m = 0; m < metadatacounts.length; m++) {
        for (let n = 0; n < nodecounts.length; n++) {
            csv[i]=["metadata_"+metadatacounts[m], nodecounts[n]].concat(instance["node_" + nodecounts[n]]["metadata_" + metadatacounts[m]]);
            i++;
        }
    }
    return JSON.parse(JSON.stringify(csv));
}

function metadataAxisCsv(instance, nodecounts, metadatacounts){
    let metadata_header = ["nodes","metadata"];
    let iterations = instance["node_" + nodecounts[0]]["metadata_" + metadatacounts[0]].length;
    for(let i = 0; i<iterations; i++){
        metadata_header.push("time_ms_" + (i+1));
    }
    let csv = [];
    csv[0] = metadata_header;
    let i = 1;
    for (let n = 0; n < nodecounts.length; n++) {
        for (let m = 0; m < metadatacounts.length; m++) {
            csv[i]=["nodes_" + nodecounts[n], metadatacounts[m] ].concat(instance["node_" + nodecounts[n]]["metadata_" + metadatacounts[m]]);
            i++;
        }
    }
    return JSON.parse(JSON.stringify(csv));
}

function nodesSpreadsheetCsv(instance, nodecounts, metadatacounts){
    let nodes_header = ["nodes"];
    for(let m = 0; m<metadatacounts.length; m++){
        nodes_header.push(metadatacounts[m]+"_metadata_mismatches");
    }

    let iterations = instance["node_" + nodecounts[0]]["metadata_" + metadatacounts[0]].length;

    let csv = [];
    csv[0] = nodes_header;
    let row_number = 1;
    for (let n = 0; n < nodecounts.length; n++) {
        for(let i = 0; i< iterations; i++) {
            let row = [];
            row.push(nodecounts[n]);
            for (let m = 0; m < metadatacounts.length; m++) {
                row.push(instance["node_" + nodecounts[n]]["metadata_" + metadatacounts[m]][i]);
            }
            csv[row_number] = row;
            row_number++;
        }
    }
    return JSON.parse(JSON.stringify(csv));
}

function metadataSpreadsheetCsv(instance, nodecounts, metadatacounts){
    let metadata_header = ["metadata"];
    for(let n = 0; n<nodecounts.length; n++){
        metadata_header.push(nodecounts[n]+"_nodes");
    }

    let iterations = instance["node_" + nodecounts[0]]["metadata_" + metadatacounts[0]].length;

    let csv = [];
    csv[0] = metadata_header;
    let row_number = 1;
    for (let m = 0; m < metadatacounts.length; m++) {
        for(let i = 0; i< iterations; i++) {
            let row = [];
            row.push(metadatacounts[m]);
            for (let n = 0; n < nodecounts.length; n++) {
                row.push(instance["node_" + nodecounts[n]]["metadata_" + metadatacounts[m]][i]);
            }
            csv[row_number] = row;
            row_number++;
        }
    }
    return JSON.parse(JSON.stringify(csv));
}

function toCsvFile(data, filename){
    return new Promise((resolve,reject)=>{
        let seperator = ",";
        let lineseperator = "\n";

        let csvString = data.map(row => row.join(seperator)).join(lineseperator);

        fs.writeFile(filename, csvString, resolve);
    });
}