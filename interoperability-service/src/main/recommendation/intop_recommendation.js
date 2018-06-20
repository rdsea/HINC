const intop_check = require('../check/intop_check');
const util = require('../util/slice_util');
const MongoClient = require("mongodb").MongoClient;

let mongodbUrl = "mongodb://test:rsihub1@ds161710.mlab.com:61710/recommendation_test";

let test_mode = false;
let test_resources = [];

exports.getRecommendationsWithoutCheck = function(slice, checkresults){
    let testslice = util.deepcopy(slice);
    return exports.applyRecommendationsWithoutCheck(testslice, checkresults);
};

exports.getRecommendations = function(slice){
    let checkresults = intop_check.checkSlice(slice);
    return exports.getRecommendationsWithoutCheck(slice, checkresults);
};

exports.applyRecommendations = function(slice){
    let checkresults = intop_check.checkSlice(slice);
    return exports.applyRecommendationsWithoutCheck(slice, checkresults);
};


exports.applyRecommendationsWithoutCheck = function(slice, checkresults){
    return new Promise(function(resolve, reject){
        /*testMongoDB().then(function(data){
            console.log(data)
        }, console.err);*/

        if(checkresults.errors.length === 0){
            resolve(slice);
        }
        let queryMockIndex = 0;
        let promises = [];
        for(let i = 0; i < checkresults.errors.length; i++) {
            //while(checkresults.errors.length>0){
            queryMockIndex ++;
            //TODO search
            promises.push(searchPromise("query"+queryMockIndex).then(function (solution_resource) {
                if(solution_resource != null) {
                    searchPromise("broker").then(function(broker){
                        solveByAddition(checkresults.errors[i], slice, solution_resource, broker);
                        //checkresults = intop_check.checkSlice(slice);
                    }, console.err);
                }
            }, console.err));
            /*if(checkresults.errors.length>=errorcount){
                break;
            }*/
            /* TODO: solution categorisation (Addition, Reduction, Substitution)
            if (checkresults.errors[i].impact.length > 0) {
                // can not be solved by reduction
            }else{
                solveBySubstitution()
            }
            */
        }

        Promise.all(promises).then(function(){resolve(slice)});

        //TODO for each problem, check which kind of problem it is (addition, reduction, substitution)
        //TODO solve problem by kind
    });
};


exports.callbackApplyRecommendationsWithoutCheck = function(slice, checkresults, cb){
    testMongoDB().then(function(data){
        console.log(data)
    }, console.err);

    if(checkresults.errors.length === 0){
        cb(slice);
    }
    let queryMockIndex = 0;
    for(let i = 0; i < checkresults.errors.length; i++) {
        //while(checkresults.errors.length>0){
        queryMockIndex ++;
        //TODO search
        searchResources("query" + queryMockIndex, checkresults, slice, cb, function (solution_resource, checkresults, slice, cb) {

            //let errorcount = checkresults.errors.length;
            if(solution_resource != null) {
                searchResources("broker", checkresults, slice, cb, function(broker, checkresults, slice, cb){
                    solveByAddition(checkresults.errors[0], slice, solution_resource, broker);
                    checkresults = intop_check.checkSlice(slice);
                    cb(slice);
                });
            }


        });

        /*if(checkresults.errors.length>=errorcount){
            break;
        }*/
        /* TODO: solution categorisation (Addition, Reduction, Substitution)
        if (checkresults.errors[i].impact.length > 0) {
            // can not be solved by reduction
        }else{
            solveBySubstitution()
        }
        */
    }

    //TODO for each problem, check which kind of problem it is (addition, reduction, substitution)
    //TODO solve problem by kind


    cb(slice);
};

function solveByAddition(error, slice, solution_resource, broker){
    if(error.cause.isDirect) {
        //if its a structural problem or protocol issue, add broker
        //otherwise there should not be a broker involved --> add transformer
        //TODO direct add broker
        let source = slice.resources[error.cause.source.nodename];
        let dest = slice.resources[error.cause.target.nodename];
        util.sliceDisConnect(slice, source, dest);

        let intopName = "intop_" + solution_resource.name;

        intopName = util.sliceAddResource(slice, solution_resource, intopName);

        util.sliceConnectById(slice, error.cause.source.nodename, intopName, intopName + "1");
        util.sliceConnectById(slice, intopName, error.cause.target.nodename, intopName + "2");
    }else{
        //if direct, check for M:N dependencies and add broker to the correct side of the transformer

        let source = slice.resources[error.cause.source.nodename];
        let origbroker = slice.resources[error.cause.path[1].nodename];
        let target = slice.resources[error.cause.target.nodename];
        //TODO check where transformation should be injected (between source/broker or broker/target)
        //TODO source/broker if: 1:N or M:N (coordinated)
        //TODO broker/target if: M:1 or M:N (coordinated)

        util.sliceDisConnect(slice, source, origbroker);

        let intopBroker = "intop_" + broker.name;
        intopBroker = util.sliceAddResource(slice, broker, intopBroker);

        let intopName = "intop_" + solution_resource.name;
        intopName = util.sliceAddResource(slice, solution_resource, intopName);


        util.sliceConnectById(slice, error.cause.source.nodename, intopBroker, intopName + "1");
        util.sliceConnectById(slice, intopBroker, intopName, intopName + "2");
        util.sliceConnectById(slice, intopName, error.cause.path[1].nodename, intopName + "3");
    }
}


function solveBySubstitution(error, slice, solution_resource, broker){

}


function solveByReduction(error, slice, solution_resource, broker){

}



exports.setTestMode = function (testMode, testResources) {
  test_mode = testMode;
  test_resources = testResources;
};

function searchPromise(error){
    return new Promise(function (resolve, reject){
        resolve(test_resources[error]);
    });
}

function searchResources(error, checkresults, slice, old_cb, cb){
    if(test_mode){
        cb(test_resources[error],checkresults, slice, old_cb);
    }
    //TODO
    cb(null, checkresults, slice, old_cb);
}

function testMongoDB(){
    /*
    function readFileAsync (file, encoding) {
  return new Promise(function (resolve, reject) {
    fs.readFile(file, encoding, function (err, data) {
      if (err) return reject(err) // rejects the promise with `err` as the reason
      resolve(data)               // fulfills the promise with `data` as the value
    })
  })
}
     */

    return new Promise(function(resolve, reject){
        MongoClient.connect(mongodbUrl, function(err, db) {
            if (err) return reject(err);
            let dbo = db.db("recommendation_test");
            let query = { "metadata.inputs.protocol.protocol_name": "mqtt" };
            dbo.collection("test").find(query).toArray(function(err, result) {
                if (err) throw err;
                db.close();
                resolve(result);
            });
        });
    });
}