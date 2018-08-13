const express = require('express')
const app = express()

const MongoClient = require("mongodb").MongoClient;
let MONGODB_URL = 'mongodb://iotcloudexamples:ac.at.tuwien.dsg@iotcloudexamples-shard-00-00-pz2vu.mongodb.net:27017,iotcloudexamples-shard-00-01-pz2vu.mongodb.net:27017,iotcloudexamples-shard-00-02-pz2vu.mongodb.net:27017/sinc?ssl=true&replicaSet=IoTCloudExamples-shard-0&authSource=admin';
const DB_NAME = "perfomance";
const COLLECTION = "test";

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
    let collection = db.collection(COLLECTION);
    let result = {
        second: req.params.second,
        nanosecond: req.params.nanosecond
    }

    collection.insert(result).catch((err) => {
        console.error(err);
    });

    res.send('success')
})

app.listen(3000, () => console.log('master listening on port 3000!'))
