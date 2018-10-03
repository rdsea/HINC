let questions = [
    {
        name: "local_count",
        message: "how many local clusters do you require?",
        default: 1,
        validate: validateNumber,
    },

    {
        name: "provider_count",
        message: "how many providers do you want per local management service?",
        default: 4,
        validate: validateNumber,
    },

    {
        name: "broker_uri",
        message: "rabbitmq broker uri connection string",
        validate: required,
    },

    {
        name: "global_mongo_db",
        message: "mongodb connection string of rsihub-global. if you don't provide one, dockerized mongodb will be provided"
    },
    {
        name: "local_mongo_db",
        message: "mongodb connection string of the locals. if you don't provide one, dockerized mongodb will be provided"
    },
    {
        name: "sas_mongo_db",
        message: "mongodb connection string of rsihub-sas (software artefact service). if you don't provide one, dockerized mongodb will be provided"
    },
    {
        name: "intop_mongo_db",
        message: "mongodb connection string of rsihub-intop (interoperability service). if you don't provide one, dockerized mongodb will be provided"
    },
    {
        name: "is_docker_stack",
        message: "do you need to deploy the stack to a docker swarm?",
        type: "confirm",
        default: true
    }
]


function validateNumber(input)
{
   var reg = /^\d+$/;
   return reg.test(input) || "Input should be a number!";
}

function required(input){
    if(input === '') return "This field is required!";

    return true;
}

module.exports = questions;