const graph_util = require('../transform/slice_to_graph');
const check_protocol = require('../check/check_protocol');
const check_dataformat = require('../check/check_dataformat');
const check_qod = require('../check/check_qod');
const check_qos = require('../check/check_qos');

const errorGenerator = require('../transform/error_generator_graph');
const matchGenerator = require('../transform/match_generator_graph');


exports.checkSlice = function(slice){
    let graph = graph_util.sliceToGraph(slice);
    let errors = [];
    let warnings = [];
    let matches = [];

    checkGraph(graph, errors, warnings, matches);

    return {errors:errors, warnings:warnings, matches: matches};
};

function checkGraph(graph, errors, warnings, matches){
    graph.startNodes.forEach(function (startNode){traverseGraph(startNode, graph, errors, warnings, matches)});
    let unmarkedNodes = graph_util.getUnmarkedNodes(graph);
    unmarkedNodes.forEach(function (node){traverseGraph(node, graph, errors, warnings, matches)});
}


function traverseGraph(node, graph, errors, warnings, matches) {
    let path = [];
    node.marked = true;
    checkConnectedNodes(node, node, true, path, null, graph, errors, warnings, matches);

    let nextNodes = graph_util.nextNodes(graph, node);
    for(let i = 0; i<nextNodes.length;i++){
        if(nextNodes[i].marked===false){
            traverseGraph(nextNodes[i], graph, errors, warnings, matches);
        }
    }
}

function checkConnectedNodes(startNode, currentNode, directConnection, path, startOutput, graph, errors, warnings, matches) {
    path.push(currentNode);
    let nextNodes = graph_util.nextNodes(graph,currentNode);
    for(let i = 0; i<nextNodes.length; i++){
        if(nextNodes[i] === startNode){
            return;
        }

        if(directConnection){
            startOutput = checkDirectConnection(startNode, nextNodes[i], path, graph, errors, warnings, matches);
        }else{
            indirectConnectionCheck(startNode, nextNodes[i], currentNode, startOutput, path, graph, errors, warnings, matches);
        }

        if(nextNodes[i].resource.metadata.resource.category === "network_function"){
            checkConnectedNodes(startNode, nextNodes[i], false, path, startOutput, graph, errors, warnings, matches);
        }
    }
}

function checkDirectConnection(sourceNode, targetNode, path, graph, errors, warnings, matches){
    console.log("check direct " + sourceNode.nodename + " "+  targetNode.nodename);

    let metadataConnectionChecks = [check_protocol.checkProtocols];
    let connection = getBestMetadataConnection(sourceNode, targetNode, metadataConnectionChecks);

    metadataConnectionChecks.push(check_dataformat.checkDataFormat);
    metadataConnectionChecks.push(check_qod.checkQoD);
    metadataConnectionChecks.push(check_qos.checkQoS);

    let checkErrors = checkMetadataConnection(sourceNode, connection.output, targetNode, connection.input, metadataConnectionChecks);

    if(checkErrors.errors.length===0){
        matches.push(matchGenerator.generateMatchObject(sourceNode, targetNode, true));
    }else{
        //generate structured error object
        let errorObject = errorGenerator.generateErrorObject(sourceNode, targetNode, checkErrors, true, path, graph);
        //TODO maybe map errors to responsible source- or targetnode
        errors.push(errorObject);
    }

    return connection.output;
}

function indirectConnectionCheck(sourceNode, targetNode, currentNode, sourceOutput, path, graph, errors, warnings, matches){
    console.log("indirect check " + sourceNode.nodename + " "+  targetNode.nodename);
    let networkToTargetChecks = [check_protocol.checkProtocols];
    //TODO ignore errors and warnings
    let networkToTarget = getBestMetadataConnection(currentNode, targetNode, networkToTargetChecks);


    let metadataConnectionChecks = [];
    metadataConnectionChecks.push(check_dataformat.checkDataFormat);
    metadataConnectionChecks.push(check_qod.checkQoD);
    metadataConnectionChecks.push(check_qos.checkQoS);

    let checkErrors = checkMetadataConnection(sourceNode, sourceOutput, targetNode, networkToTarget.input, metadataConnectionChecks);

    if(checkErrors.errors.length===0){
        matches.push(matchGenerator.generateMatchObject(sourceNode, targetNode, false));
    }else{
        //generate structured error object
        let errorObject = errorGenerator.generateErrorObject(sourceNode, targetNode, checkErrors, false, path, graph);
        //TODO maybe map errors to responsible source- or targetnode
        errors.push(errorObject);
    }
}

function checkMetadataConnection(sourceNode, sourceOutput, targetNode, targetInput, checkFunctionArray){
    let errors = [];
    let warnings =[];


    for(let f = 0; f < checkFunctionArray.length; f++){
        let checkfunction = checkFunctionArray[f];
        checkfunction(sourceNode.resource.metadata, sourceOutput,
                        targetNode.resource.metadata, targetInput,
                        errors, warnings);
    }

    return {errors: errors, warnings:warnings}
}

function getBestMetadataConnection(sourceNode, targetNode, checkFunctionArray){
    let matchingInOutputs = protocolMatchingOutInputs(sourceNode.resource, targetNode.resource);

    if(matchingInOutputs.length === 0){
        return firstOutInputPair(sourceNode.resource, targetNode.resource);
    }

    let bestConnection = matchingInOutputs[0];

    //TODO for now it is assumed that max. one connection is optimal

    for(let i = 0; i < matchingInOutputs.length; i++){
        matchingInOutputs[i].errors = [];
        matchingInOutputs[i].warnings = [];


        for(let f = 0; f < checkFunctionArray.length; f++){
            let checkfunction = checkFunctionArray[f];
            checkfunction(sourceNode.resource.metadata, matchingInOutputs[i].output, targetNode.resource.metadata, matchingInOutputs[i].input,
                matchingInOutputs[i].errors, matchingInOutputs[i].warnings);
        }


        if(matchingInOutputs[i].errors.length <= 0 &&
            matchingInOutputs[i].warnings.length <= 0 ){
            bestConnection = matchingInOutputs[i];
            break;
        }

        // save connection with lowest number of errors and warnings
        if(matchingInOutputs[i].errors.length<bestConnection.errors.length
            || (matchingInOutputs[i].errors.length===bestConnection.errors.length &&
                matchingInOutputs[i].warnings.length<bestConnection.warnings.length)){
            bestConnection = matchingInOutputs[i];
        }
    }
    return bestConnection;
}

function protocolMatchingOutInputs(source, target){
    let protocolmatches = [];
    let outputs = [];
    let inputs = [];

    if(source.metadata.outputs){
        outputs = source.metadata.outputs;
    }
    if(target.metadata.inputs){
        inputs = target.metadata.inputs;
    }

    addBrokerOutinputs(inputs, target, "input");
    addBrokerOutinputs(outputs, source, "output");


    for(let i = 0; i<outputs.length; i++){
        for(let j=0; j<inputs.length; j++){
            if(outputs[i].protocol.protocol_name === inputs[j].protocol.protocol_name){
                protocolmatches.push({output: outputs[i], input:inputs[j]});
            }
        }
    }

    return protocolmatches;
}



function firstOutInputPair(source, target){
    let outputs = [];
    let inputs = [];

    if(source.metadata.outputs){
        outputs = source.metadata.outputs;
    }
    if(target.metadata.inputs){
        inputs = target.metadata.inputs;
    }

    addBrokerOutinputs(inputs, target, "input");
    addBrokerOutinputs(outputs, source, "output");


    return {output: outputs[0], input:inputs[0]};
}


function addBrokerOutinputs(outinputs, resource, type){
    if(resource.metadata.resource.category === "network_function"){
        if(resource.metadata.resource.type.prototype === "messagebroker"){
            for(let i = 0; i < resource.metadata.resource.type.protocols.length; i++){

                //mqtt || amqp
                if(resource.metadata.resource.type.protocols[i].protocol_name === "mqtt" ||
                    resource.metadata.resource.type.protocols[i].protocol_name === "mqtts"){
                    for(let m = 0; m< resource.metadata.resource.type.topics.length; m++){
                        let outinput = {};
                        outinput.push_pull = "push";
                        outinput.protocol = resource.metadata.resource.type.protocols[i];
                        outinput.topic = resource.metadata.resource.type.topics[m];
                        outinputs.push(outinput);
                    }
                }
                if(resource.metadata.resource.type.protocols[i].protocol_name === "amqp" ||
                    resource.metadata.resource.type.protocols[i].protocol_name === "amqps"){

                    if(type === "input" && resource.metadata.resource.type.exchanges) {
                        for (let m = 0; m < resource.metadata.resource.type.exchanges.length; m++) {
                            let outinput = {};
                            outinput.push_pull = "push";
                            outinput.protocol = resource.metadata.resource.type.protocols[i];
                            outinput.exchange = resource.metadata.resource.type.exchanges[m];
                            outinputs.push(outinput);
                        }
                    }else if(type === "output" && resource.metadata.resource.type.queues) {
                        for (let m = 0; m < resource.metadata.resource.type.queues.length; m++) {
                            let outinput = {};
                            outinput.push_pull = "push";
                            outinput.protocol = resource.metadata.resource.type.protocols[i];
                            outinput.queue = resource.metadata.resource.type.queues[m].name;
                            outinputs.push(outinput);
                        }
                    }
                }
            }
        }
    }
}
