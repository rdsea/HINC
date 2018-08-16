exports.command = 'query-intop <options>'
exports.desc = 'queries interoperability information based on provided options <key>=<value>'
exports.builder = {}
exports.handler = function (argv) {
    console.log(JSON.stringify(result, null, 4));
}
let cmd = `node pizza.js query-intop input-data-format=JSON \
                output-data-format=CSV \
                input_protocol=HTTP \
                output_protocol=AMQP            
`

let result = {  
    "input_protocol":"HTTP",
    "output_protocol":"AMQP",
    "input_data_format":"JSON",
    "output_data_format":"CSV",
    "available_artifact":{  
        "name":"js-artifact-runner",
        "resourceType":"SOFTWARE_ARTIFACE",
        "location":"null",
        "parameters":{  
            "amqp_uri":"amqp connection string",
            "amqp_queue":"amqp queue"
        },
        "metadata":{  
            "software_artefact_ref":"softwareartefact1523451460864",
            "category":"datatransformer",
            "input":{  
                "protocol":"HTTP",
                "data_format":"JSON",
                "url":"http://datatransformer14831:8080/"
            },
            "output":{  
                "protocol":"AMQP",
                "dataformat":"CSV",
                "url":"<PENDING_CONFIGURATION>",
                "queue":"<PENDING_CONFIGURATION>",
                "username":"NOT_REQUIRED",
                "password":"NOT_REQUIRED"
            }
        }
    }
}