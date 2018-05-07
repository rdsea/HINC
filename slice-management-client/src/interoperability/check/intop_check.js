const sliceToConnectionArray = require('../transform/slice_to_connection_array');
const events = require('events');
const eventEmitter = new events.EventEmitter();
const check_protocol = require('../check/check_protocol');


exports.check = function (slice) {

    let connections = sliceToConnectionArray.sliceToConnectionArray(slice);
    let errors = [];
    let warnings = [];

    /*eventEmitter.on('protocol', check_protocol.checkProtocols);
    connections.forEach(emitEvents(eventEmitter, errors, warnings));*/

    connections.forEach(checkConnection(errors, warnings));

    return {errors:errors, warnings:warnings};
};

exports.checkSingleConnection = function(connection){
    let errors = [];
    let warnings = [];

    let connections = [connection];
    connections.forEach(checkConnection(errors, warnings));

    return {errors:errors, warnings:warnings};
};

function checkConnection(errors, warnings) {
    return function (connection) {
        //TODO find matching input/output

        //IF source and target are not a VNF:
        //connection.source.metadata.outputs.protocol_name
        //connection.target.metadata.inputs.protocol_name
        // SELECT protocol check

        //TODO handle multiple matches for resources


        //TODO check metadata of input/output pair
        //TODO if multiple metadata mismatches, prioritize which input/output pair is most appropriate

        //TODO create interoperability error if no match is found
    }
}


function emitEvents(eventEmitter, errors, warnings) {
    return function(connection){
        console.log("emit" + connection.connectionId);

        //eventEmitter.emit("protocol", connection.source, connection.target, errors, warnings);



        //TODO find matching input/output
        //TODO handle multiple matches for resources

        //TODO emit events for input/output-pair
        //TODO emit error if no match is found


        /* overall software design:
            -emitter, emits checkEvents with data to check and check-classification
            -checks, are registered to an "eventtype" and check the interoperability regarding the eventtype
         */

        /*eventtypes:
            - qod
            - qos
            - dataformat
            - protocol

            - (datacontract + userrequest)
         */


        //emit - structure
        // eventName:
        // connection: the connection object of the slice, that connects source and target
        // sourceResource: the source resource
        // targetResource: the target resource
        //
        // errors: datastructure that collects all errors
        // warnings datastructure that collects all warnings



        //simple, direct checks (simply eval both objects everytime):
        // qos
        // qod
        // data contract

        //complex, indirect checks (the check depends on the objects):
        // protocol/input-output --> network functions don't have protocol metadata as input/output
        // resourcetypes --> checks that correspond to the two objects involved

        // request and resources --> checks against the user's request are different from graph-tests (they should check every resource, not the connection between them)




        /* actual interoperability checks:
        qos / required_qos
        qod / required_qod
        datacontract / user_request


        protocol+dataformat of input/output/resource
         */
    }
}