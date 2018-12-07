const Promise = require('bluebird');
const GoogleCloudStorage = Promise.promisifyAll(require('@google-cloud/storage'));
const path = require('path');


const storage = GoogleCloudStorage({
    keyFilename: "google_storage_key.json"
});

const BUCKET_NAME = "mytestbucket_test";
const myBucket = storage.bucket(BUCKET_NAME);

module.exports = {upload: upload};


function _getPublicThumbnailUrlForItem(filename) {
    return new Promise((resolve) => {resolve(`https://storage.googleapis.com/${BUCKET_NAME}/${filename}`)});
}

function upload(filename){
    let filePath= path.resolve(filename);
    return myBucket.uploadAsync(filePath, { destination: `results/${filename}` }).then(()=>{
        return _getPublicThumbnailUrlForItem(filename);
    });
}
