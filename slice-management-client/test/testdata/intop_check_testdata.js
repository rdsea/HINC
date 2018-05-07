
//*********************************************************************************************** helper functions

exports.emptyIoTResource = function(id) {
    return {metadata: {id:id, resource:{category:"iot"}}};
};

exports.connectResources = function (source, destination, connectionMetadata){
    //metadata connection (metadata of source and destination will match)
    if(!source.metadata.hasOwnProperty("output")){
        source.metadata.output = [connectionMetadata];
    }else {
        source.metadata["output"].push(connectionMetadata);
    }
    if(!destination.metadata.hasOwnProperty("input")){
        destination.metadata.input = [connectionMetadata];
    }else {
        destination.metadata["input"].push(connectionMetadata);
    }
};

addInput = function (resource, inputMetadata) {
    if(!resource.metadata.hasOwnProperty("input")){
        resource.metadata.input = [inputMetadata];
    }else {
        resource.metadata["input"].push(inputMetadata);
    }
};

addOutput = function (resource, outputMetadata) {
    if(!resource.metadata.hasOwnProperty("output")){
        resource.metadata.output = [outputMetadata];
    }else {
        resource.metadata["output"].push(outputMetadata);
    }
};

exports.inOut_metadata_example = function(){
   return {
           push_pull: "push",
           protocol: {
               uri: "mqtt:123.456.78.9:1883",
               protocol_name: "mqtt",
               version: "3.1.1",
               topic: "sensor_output",
               qos: 0,
               keep_alive: 0,
               will_required: false,
               implementation: "mqtt.js",
               implementation_version: "1.4.15"
           },
           dataformat: {
               encoding: "UTF-8",
               dataformat_name: "csv",
               seperator: ";",
               newline_seperator: "\n",
               headers: "time; value"
           },
           qos: {
               data_interval: "2000"
           }
    }
};


exports.amqp_broker_example = function(){
  return {
      metadata :{
          resource: {
              category:"network_function",
              type:{
                  prototype:"messagebroker",
                  broker: "RabbitMQ",
                  broker_version: "3.7.3",
                  protocols: [{
                      uri: "amqp:123.456.78.9:5672",
                      protocol_name: "amqp",
                      version: "0.9.1"
                  }],
                  queues: [{
                      name: "sensor_queue",
                      durable: true
                  }],
                  exchanges: ["sensor_output"],
                  bindings: [{
                      exchange: "sensor_output",
                      queue:"sensor_queue",
                      type:"fanout"
                  }],
                  auto_create: false
              }
          }
      }
  }
};

exports.broker_compatible_output = function(){
    return {
        push_pull: "push",
        protocol: {
            uri: "amqp:123.456.78.9:5672",
            protocol_name: "amqp",
            version: "0.9.1",
            exchange: "sensor_output"
        },
        dataformat: {
            encoding: "UTF-8",
            dataformat_name: "csv",
            seperator: ";",
            newline_seperator: "\n",
            headers: "time; value"
        }
    }
};

exports.broker_compatible_input = function(){
    return {
        push_pull: "push",
        protocol: {
            uri: "amqp:123.456.78.9:5672",
            protocol_name: "amqp",
            version: "0.9.1",
            queue: "sensor_queue"
        },
        dataformat: {
            encoding: "UTF-8",
            dataformat_name: "csv",
            seperator: ";",
            newline_seperator: "\n",
            headers: "time; value"
        }
    }
};


//*********************************************************************************************** single inout


exports.single_inout_matching = function () {
    let connection = {};
    connection.connectionId = "testdata_matching_single_inout";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.emptyIoTResource("r2");

    exports.connectResources(connection.source, connection.target, exports.inOut_metadata_example());

    return connection;
};

exports.single_inout_mismatching_protocol = function () {
    let connection = {};
    connection.connectionId = "single_inout_mismatching_protocol";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.emptyIoTResource("r2");

    exports.connectResources(connection.source, connection.target, exports.inOut_metadata_example());
    connection.source.metadata.output[0].protocol = {
        uri: "amqp:123.456.78.9:5672",
        protocol_name: "amqp",
        version: "0.9.1",
        exchange: "sensor_output"
    };

    return connection;
};

exports.single_inout_mismatching_protocolQos = function () {
    let connection = {};
    connection.connectionId = "single_inout_mismatching_protocolQos";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.emptyIoTResource("r2");

    exports.connectResources(connection.source, connection.target, exports.inOut_metadata_example());

    connection.target.metadata.input[0].protocol.qos = 1;

    return connection;
};


//*********************************************************************************************** multiple inout

exports.multiple_inout_matching = function () {
    let connection = {};
    connection.connectionId = "multiple_inout_matching";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.emptyIoTResource("r2");

    addOutput(connection.source, {protocol:{
        uri: "amqp:123.456.78.9:5672",
        protocol_name: "amqp_source",
        version: "0.9.1",
        exchange: "sensor_output"
    }});



    exports.connectResources(connection.source, connection.target, exports.inOut_metadata_example());


    addInput(connection.target, {protocol:{
            uri: "amqp:123.456.78.9:5672",
            protocol_name: "amqp_target",
            version: "0.9.1",
            exchange: "sensor_output"
    }});

    return connection;
};

exports.multiple_inout_mismatching_protocol = function () {
    let connection = {};
    connection.connectionId = "multiple_inout_mismatching_protocol";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.emptyIoTResource("r2");


    addOutput(connection.source, {protocol:{
            uri: "amqp:123.456.78.9:5672",
            protocol_name: "output1_source",
            version: "0.9.1",
            exchange: "sensor_output"
        }});

    addOutput(connection.source, {protocol:{
            uri: "amqp:123.456.78.9:5672",
            protocol_name: "output2_source",
            version: "0.9.1",
            exchange: "sensor_output"
        }});

    addInput(connection.target, {protocol:{
            uri: "amqp:123.456.78.9:5672",
            protocol_name: "input1_target",
            version: "0.9.1",
            exchange: "sensor_output"
        }});

    addInput(connection.target, {protocol:{
            uri: "amqp:123.456.78.9:5672",
            protocol_name: "input2_target",
            version: "0.9.1",
            exchange: "sensor_output"
    }});

    return connection;
};

exports.multiple_inout_mismatching_protocolQos = function () {
    let connection = {};
    connection.connectionId = "multiple_inout_mismatching_protocolQos";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.emptyIoTResource("r2");

    exports.connectResources(connection.source, connection.target, exports.inOut_metadata_example());

    connection.target.metadata.input[0].protocol.qos = 1;


    addOutput(connection.source, {protocol:{
            uri: "amqp:123.456.78.9:5672",
            protocol_name: "amqp_source",
            version: "0.9.1",
            exchange: "sensor_output"
    }});

    addInput(connection.target, {protocol:{
            uri: "amqp:123.456.78.9:5672",
            protocol_name: "amqp_target",
            version: "0.9.1",
            exchange: "sensor_output"
    }});

    return connection;
};



exports.multiple_inout_matching_one_mismatching_protocolQos = function () {
    let connection = {};
    connection.connectionId = "multiple_inout_mismatching_protocolQos";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.emptyIoTResource("r2");

    exports.connectResources(connection.source, connection.target, exports.inOut_metadata_example());

    connection.target.metadata.input[0].protocol.qos = 1;

    exports.connectResources(connection.source, connection.target, {protocol:{
            uri: "amqp:123.456.78.9:5672",
            protocol_name: "amqp_source",
            version: "0.9.1",
            exchange: "sensor_output"
        }});
};



//*********************************************************************************************** broker output

exports.broker_output_matching = function () {
    let connection = {};
    connection.connectionId = "broker_output_matching";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.amqp_broker_example();

    addOutput(connection.source, exports.broker_compatible_output());

    return connection;
};

exports.broker_output_mismatching_protocol = function () {
    let connection = {};
    connection.connectionId = "broker_output_mismatching_protocol";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.amqp_broker_example();

    addOutput(connection.source, exports.inOut_metadata_example());

    return connection;
};

//*********************************************************************************************** broker input
exports.broker_input_matching = function () {
    let connection = {};
    connection.connectionId = "broker_input_matching";
    connection.source = exports.amqp_broker_example();
    connection.target = exports.emptyIoTResource("r1");

    addInput(connection.target, exports.broker_compatible_input());

    return connection;
};
exports.broker_input_mismatching_protocol = function () {
    let connection = {};
    connection.connectionId = "broker_input_mismatching_protocol";
    connection.source = exports.amqp_broker_example();
    connection.target = exports.emptyIoTResource("r1");

    addInput(connection.target, exports.inOut_metadata_example());

    return connection;
};

//*********************************************************************************************** no input/output

exports.no_input = function () {
    let connection = {};
    connection.connectionId = "no_input";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.emptyIoTResource("r2");

    addOutput(connection.source, exports.inOut_metadata_example());

    return connection;
};


exports.no_output = function () {
    let connection = {};
    connection.connectionId = "no_output";
    connection.source = exports.emptyIoTResource("r1");
    connection.target = exports.emptyIoTResource("r2");

    addInput(connection.target, exports.inOut_metadata_example());

    return connection;
};