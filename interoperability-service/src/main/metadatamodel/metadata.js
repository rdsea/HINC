metadata = {
    category:"",

    categoryspecific : {},

    input:[{
        pattern:"see Patterns",
        protocol:"see Protocols",
        uri:"a valid URI",
        dataformat:"see Dataformats"
    }],
    output:[{
    }]
};



// Protocols TODO: add protocolspecific metadata, eventually add protocols
mqtt = {topic:"", qos:"At most once (0), At least once (1), Exactly once (2).", version:"v3.1.1.", keep_alive:""};
amqp = {};
tcp = {};
udp = {};
coap = {};
http = {};
rest = "???";
protocols = [mqtt, amqp, http, tcp, udp, coap];

// Dataformats TODO: add specific metadata, eventually add/remove formats
csv = {seperator:",", newline_seperator:"\r\n", headers:["header1","header2"]};
json = {};
xml = {};
avro = {};
rdf = {};
videoformats = "???";

// Data Contracts

// Data Quality (including frequency etc)

// Data Regulation


//sensor
metadata = {
    resource: {
        category: "iot",
        type:{
            prototype: "sensor",
            unit: "°C",
            sampling_interval: "1000",
            precision: "0.01",
            range_minValue: "-50",
            range_maxValue: "60",
            location: "outside",
            latitude: "48.1986582",
            longitude: "16.3662739"
        },
        datacontract:{
            data_rights:{
                derivation : false,
                collection : false,
                reproduction : false,
                commercial_usage : false,
                attribution : false
            },
            pricing: {
                price: "1",
                currency: "EUR",
                price_per: "hour"
            },
            regulation:{
                jurisdiction: "Austria",
                data_regulation_acts: "Regulation (EU) 2016/679"
            }

        },
        qos:{
            data_interval: "1000",
            reliability_level: "8",
            availability: "90",
            bit_rate: "0.1",
            bit_rate_unit: "Mbit/s",
            connection_limit: "10"
        }
    },
    inputs: [],
    outputs: [{
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
    },{
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
    }]
};


//messagebroker
metadata = {
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
};




