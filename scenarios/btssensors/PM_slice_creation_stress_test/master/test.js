const MongoClient = require("mongodb").MongoClient;

MONGODB_URL = "mongodb://35.240.93.210:20717/test"
DB_NAME = "performance"
let client = null;
let db = null;

MongoClient.connect(MONGODB_URL, {useNewUrlParser: true}).then((c) => {
    client = c;
    db = client.db(DB_NAME);
});