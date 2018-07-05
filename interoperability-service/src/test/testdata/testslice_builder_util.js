module.exports = {
    empty_slice:empty_slice,
    source:source,
    dest:dest,
    mqttBroker:mqttBroker,
    amqpBroker:amqpBroker,
    mqttProtocol:mqttProtocol,
    amqpProtocol:amqpProtocol,
    httpProtocol:httpProtocol,
    csvFormat:csvFormat,
    jsonFormat:jsonFormat,
    csvToJsonArtefact:csvToJsonArtefact,
    httpPoller:httpPoller,
    httpBuffer:httpBuffer
};



function empty_slice(){
    return {resources:{},connectivities:{}};
}

function source(name, push_pull, protocol, format){
    return {
        name:name,
        source: [],
        target: [],
        metadata:{
            resource:{category:"iot", type:{prototype:"sensor"}},
            inputs: [],
            outputs:[
                {
                    push_pull:push_pull,
                    protocol:protocol,
                    dataformat:format
                }
            ]
        }
    };
}

function dest(name, push_pull, protocol, format){
    return {
        name:name,
        source: [],
        target: [],
        metadata:{
            resource:{category:"iot", type:{prototype:"storage"}},
            inputs:[
                {
                    push_pull:push_pull,
                    protocol:protocol,
                    dataformat:format
                }
            ],
            outputs:[]
        }
    };
}

function mqttBroker(name, mqttprotocol){
    return {
        name:name,
        source:[],
        target:[],
        metadata: {
            resource: {
                category:"network_function",
                type:{
                    prototype: "messagebroker",
                    protocols: [mqttprotocol],
                    topics: [mqttprotocol.topic]
                }
            }
        }
    }
}



function amqpBroker(name, amqpprotocol){
    return {
        name:name,
        source:[],
        target:[],
        metadata: {
            resource: {
                category:"network_function",
                type: {
                    prototype: "messagebroker",
                    protocols: [amqpprotocol],
                    queues: [amqpprotocol.queue],
                    exchanges: [amqpprotocol.exchange]
                }
            }
        }
    }
}

function mqttProtocol(){
    return {uri:"mqtt://test:1883",
        protocol_name:"mqtt",
        topic:"basic_test",
        qos:0}
}


function amqpProtocol() {
    return {
        uri:"amqp://test:5672",
        protocol_name:"amqp",
        queue:"testqueue",
        exchange:"testexchange",
    }
}


function httpProtocol(){
    return {
        uri: "http://test:80",
        protocol_name: "http",
        http_method: "get"
    }
}

function csvFormat() {
    return {encoding:"utf-8",
        dataformat_name:"csv"}
}

function jsonFormat() {
    return {encoding:"utf-8",
        dataformat_name:"json"}
}



function csvToJsonArtefact(name, protocol){
    return {
        name:name,
        source: [],
        target: [],
        metadata:{
            resource:{category:"iot", type:{prototype:"software_artefact"}},
            inputs:[
                {
                    push_pull:"push",
                    protocol:protocol,
                    dataformat:csvFormat()
                }
            ],
            outputs:[
                {
                    push_pull:"push",
                    protocol:protocol,
                    dataformat:jsonFormat()
                }
            ]
        }
    };
}

function httpPoller(name, dataformat){
    return {
        name:name,
        source: [],
        target: [],
        metadata:{
            resource:{category:"iot", type:{prototype:"software_artefact"}},
            inputs:[
                {
                    push_pull:"pull",
                    protocol:httpProtocol(),
                    dataformat:dataformat
                }
            ],
            outputs:[
                {
                    push_pull:"push",
                    protocol:mqttProtocol(),
                    dataformat:dataformat
                }
            ]
        }
    };
}

function httpBuffer(name, dataformat){
    return {
        name:name,
        source: [],
        target: [],
        metadata:{
            resource:{category:"iot", type:{prototype:"software_artefact"}},
            inputs:[
                {
                    push_pull:"push",
                    protocol:mqttProtocol(),
                    dataformat:dataformat
                }
            ],
            outputs:[
                {
                    push_pull:"pull",
                    protocol:httpProtocol(),
                    dataformat:dataformat
                }
            ]
        }
    };
}
