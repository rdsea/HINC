const check = require("../../main/check/intop_check")
const recommendation = require("../../main/recommendation/intop_recommendation")


module.exports = {
    recommendation:_recommendation,
    check:_check
}

function _recommendation(slice){
    let starttime = process.hrtime();
    return recommendation.getRecommendations(slice).then((result)=>{
        let diff = process.hrtime(starttime);
        result.time = diff;
        return new Promise((resolve,reject)=>{
            resolve(result);
        })
    });
}

function _check(slice){
    return new Promise((resolve,reject)=>{
        let starttime = process.hrtime();
        let result = check.checkSlice(slice);
        let diff = process.hrtime(starttime);
        result.time = diff;
        resolve(result);
    })
}