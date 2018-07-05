const builder = require("testslice_builder_util");

exports.solutionResources_test_0_0 = function (){
    let resources = {};
    return resources;
};

exports.solutionResources_test_0_1 = function (){
    let resources = {};
    //csv to json
    resources["query1"] = builder.csvToJsonArtefact("transformer", builder.httpProtocol());
    return resources;
};

exports.solutionResources_test_0_2 = function (){
    //csv to json
    //broker
    let resources = {};
    resources["query1"]=builder.csvToJsonArtefact("transformer", builder.mqttProtocol());
    resources["broker"]=builder.mqttBroker("newbroker", builder.mqttProtocol());
    return resources;
};

exports.solutionResources_test_0_3 = function (){
    //mqtt broker
    let resources = {};
    resources["query1"] = builder.mqttBroker("newbroker", builder.mqttProtocol());
    return resources;
};

exports.solutionResources_test_0_4 = function (){
    //reduction test case --> nothing needed
    let resources = {};
    return resources;
};

exports.solutionResources_test_0_5 = function (){
    //http poller
    //http buffer
    let resources = {};
    resources["query1"]=builder.httpPoller("poller", builder.jsonFormat());
    resources["query2"]=builder.httpBuffer("buffer", builder.jsonFormat());
    return resources;
};


exports.solutionResources_test_0_6 = function (){
    let resources = {};
    //broker
    resources["query1"] = builder.mqttBroker("broker", builder.mqttProtocol());
    return resources;
};

exports.solutionResources_test_0_7 = function (){
    let resources = {};
    //csv to json
    resources["query1"] = builder.csvToJsonArtefact("transformer", builder.mqttProtocol());
    resources["broker"] = builder.mqttBroker("broker", builder.mqttProtocol());
    return resources;
};

exports.solutionResources_test_0_8 = function (){
    //csv to json
    //broker
    let resources = {};
    resources["query1"]=builder.csvToJsonArtefact("transformer", builder.mqttProtocol());
    resources["broker"]=builder.mqttBroker("newbroker", builder.mqttProtocol());
    return resources;
};

exports.solutionResources_test_0_9 = function (){
    //csv to json
    //broker
    let resources = {};
    resources["query1"]=builder.csvToJsonArtefact("transformer", builder.mqttProtocol());
    resources["broker"]=builder.mqttBroker("newbroker", builder.mqttProtocol());
    return resources;
};

exports.solutionResources_test_0_10 = function (){
    //csv to json
    //broker
    let resources = {};
    resources["query1"]=builder.csvToJsonArtefact("transformer", builder.mqttProtocol());
    resources["query2"]=resources["query1"];
    resources["broker"]=builder.mqttBroker("newbroker", builder.mqttProtocol());
    return resources;
};


exports.solutionResources_test_1_0 = function (){
    let resources = {};
    return resources;
};

exports.solutionResources_test_1_1 = function (){
    //broker
    //csv to json
    let resources = {};
    resources["transformer"]=builder.csvToJsonArtefact("transformer", builder.mqttProtocol());
    resources["broker"]=builder.mqttBroker("broker", builder.mqttProtocol());
    return resources;
};

exports.solutionResources_test_1_2 = function (){
    //broker
    //csv to json
    let resources = {};
    resources["transformer"]=builder.csvToJsonArtefact("transformer", builder.mqttProtocol());
    resources["broker"]=builder.mqttBroker("broker", builder.mqttProtocol());
    return resources;
};
