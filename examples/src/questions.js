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
        name: "database_uri",
        message: "mongodb connection string, if you don't provide one, dockerized mongodb will be provided",
        default: "mongodb://root:example@mongodb:27017"
    },
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