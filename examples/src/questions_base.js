let questions_base = [
    {
        name: "is_docker_stack",
        message: "do you need to deploy the stack to a docker swarm?",
        type: "confirm",
        default: true
    },
    {
        name: "local_count",
        message: "how many local clusters do you require?",
        default: 1,
        validate: validateNumber,
    },

    {
        name: "broker_uri",
        message: "rabbitmq broker uri connection string",
        validate: required,
    },
    {
        name: "global_mongo_db",
        message: "mongodb connection string of rsihub-global. if you don't provide one, dockerised mongodb will be provided"
    },
    {
        name: "local_mongo_db",
        message: "mongodb connection string of the locals. if you don't provide one, dockerised mongodb will be provided"
    },
    {
        name: "sas_mongo_db",
        message: "mongodb connection string of rsihub-sas (software artefact service). if you don't provide one, dockerised mongodb will be provided"
    },
    {
        name: "intop_mongo_db",
        message: "mongodb connection string of rsihub-intop (interoperability service). if you don't provide one, dockerised mongodb will be provided"
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

module.exports = questions_base;