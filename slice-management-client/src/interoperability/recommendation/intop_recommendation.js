const sliceToConnectionArray = require('../transform/slice_to_connection_array');
const events = require('events');
const eventEmitter = new events.EventEmitter();
const check_protocol = require('../check/check_protocol');


exports.check = function (slice) {

    let connections = sliceToConnectionArray.sliceToConnectionArray(slice);
    let errors = [];
    let warnings = [];
    let matches = [];

    connections.forEach(checkConnection(errors, warnings, matches));

    return {errors:errors, warnings:warnings, matches: matches};
};

exports.checkSingleConnection = function(connection){
    let errors = [];
    let warnings = [];
    let matches = [];

    let connections = [connection];
    connections.forEach(checkConnection(errors, warnings, matches));

    return {errors:errors, warnings:warnings, matches: matches};
};

function checkConnection(errors, warnings, matches) {
    return function (connection) {
        let matchingInOutputs = protocolMatchingOutInputs(connection.source, connection.target);
        let matchAvailable = false;

        if(matchingInOutputs.length === 0){
            //TODO add more useful error object
            errors.push("error");
            return;
        }

        let bestConnection = matchingInOutputs[0];

        for(let i = 0; i < matchingInOutputs.length; i++){
            matchingInOutputs[i].errors = [];
            matchingInOutputs[i].warnings = [];

            check_protocol.checkProtocols(matchingInOutputs[i].output, matchingInOutputs[i].input, matchingInOutputs[i].errors ,
                matchingInOutputs[i].warnings);

            if(matchingInOutputs[i].errors.length <= 0 &&
                matchingInOutputs[i].warnings.length <= 0 ){
                //TODO add more useful match object
                matches.push(matchingInOutputs[i].output.protocol.protocol_name);
                matchAvailable = true;
                break;
            }

            // save connection with lowest number of errors and warnings
            if(matchingInOutputs[i].errors.length<bestConnection.errors.length
                || (matchingInOutputs[i].errors.length===bestConnection.errors.length &&
                    matchingInOutputs[i].warnings.length<bestConnection.warnings.length)){
                bestConnection = matchingInOutputs[i];
            }
        }

        if(!matchAvailable){
            if(bestConnection.errors.length>0){
                //TODO add more useful error object
                errors.push("error");
            }else if (bestConnection.warnings.length>0){
                //TODO add more useful warning object
                warnings.push("warning");
            }
        }
    }
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


function addBrokerOutinputs(outinputs, resource, type){
    if(resource.metadata.resource.category === "network_function"){
        if(resource.metadata.resource.type.prototype === "messagebroker"){
            for(let i = 0; i < resource.metadata.resource.type.protocols.length; i++){

                //mqtt || amqp
                if(resource.metadata.resource.type.protocols[i].protocol_name === "mqtt" ||
                    resource.metadata.resource.type.protocols[i].protocol_name === "mqtts"){
                    for(let m = 0; m< resource.metadata.resource.type.protocols[i].topics.length; m++){
                        let outinput = {};
                        outinput.push_pull = "push";
                        outinput.protocol = resource.metadata.resource.type.protocols[i];
                        outinput.topic = resource.metadata.resource.type.protocols[i].topics[m];
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

