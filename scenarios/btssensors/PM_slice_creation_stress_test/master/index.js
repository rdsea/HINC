const express = require('express')
const app = express()

const MongoClient = require("mongodb").MongoClient;
let MONGODB_URL = "mongodb://35.240.93.210:27017/test"
let DB_NAME = "perfomance";
let COLLECTION = "test";

// an environment variable can also be passed
if(process.env.MONGODB_URL){
    MONGODB_URL = process.env.MONGODB_URL
    COLLECTION = process.env.COLLECTION
}

let client = null;
let db = null;

MongoClient.connect(MONGODB_URL, {useNewUrlParser: true}).then((c) => {
    client = c;
    db = client.db(DB_NAME);
});

app.get('/result/:second/:nanosecond', (req, res) => {
    timestamp = Date.now();
    let collection = db.collection(COLLECTION);
    let result = {
        second: req.params.second,
        nanosecond: req.params.nanosecond,
        timestamp
    }

    collection.insert(result).catch((err) => {
        console.error(err);
    });

    res.send('success')
})

app.listen(3000, () => console.log('master listening on port 3000!'))
