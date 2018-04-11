const Datastore = require('nedb');
const path = require('path');

let resourceDb = new Datastore({filename: path.resolve(__dirname, './.resources.db'), autoload: true});
let providerDb = new Datastore({filename: path.resolve(__dirname, './.providers.db'), autoload: true});
let sliceDb = new Datastore({filename: path.resolve(__dirname, './.slices.db'), autoload: true});


function insert(database){
    return (doc) => {
        return new Promise((resolve, reject) => {
            console.debug(`inserting document ${JSON.stringify(doc)}`);
            database.insert(doc, (err) => {
                if(err) {
                    console.err(err);
                    reject(err);
                }else{
                    console.debug(`successfully inserted document ${JSON.stringify(doc)}`);
                    resolve(doc);
                }
            })
        });
    }    
}

function find(database){
    return (query) => {
        return new Promise((resolve, reject) => {
            database.find(query, (err, docs) => {
                if(err){
                    reject(err);
                }else{
                    console.debug(`successfully found ${docs.length} results for ${query}`);
                    resolve(docs);
                }
            });
        });
    }
    
}

function findOne(database){
    return (query) => {
        return new Promise((resolve, reject) => {
            console.debug(`finding one documentof ${JSON.stringify(query)}`);
            database.findOne(query, (err, doc) => {
                if(err){
                    console.err(err);
                    reject(err);
                }else{
                    if(doc === null) console.debug(`no result found for ${JSON.stringify(query)}`);
                    resolve(doc);
                }
            });
        });
    }
}

function update(database){
    return (query, update, options) => {
        return new Promise((resolve, reject) => {
            console.debug(`updating documents ${JSON.stringify(query)} with ${JSON.stringify(update)}`);
            database.update(query, update, (err, numberOfUpdated, upsert) => {
                if(err){
                    console.err(err);
                    reject(err);
                }else{
                    console.debug(`successfully updated ${numberOfUpdated} docs with ${upsert} upserts`);
                    let res = {
                        numberOfUpdated,
                        upsert,
                    }
                    resolve(res);
                }
            });
        })
    }
    
}

function remove(database){
    return (query, options) =>  {
        return new Promise((resolve, reject) => {
            database.remove(query, options, (err, numRemoved) => {
                if(err){
                    console.err(er);
                    reject(err);
                }else{
                    console.debug(`successfully removed ${numRemoved} entries from db`);
                    resolve();
                }
            })
        })
    }
    
}

function findOrUpdate(database){
    return (query) => {
        return new Promise((resolve, reject) => {
            database.findOne(query, (err, doc) => {
                if(err){
                    console.error(err);
                    reject(err);
                }else{
                    if(doc === null) {
                        database.insert(doc, (err) => {
                            if(err) {
                                console.err(err);
                                reject(err);
                            }else{
                                console.debug(`successfully inserted document ${JSON.stringify(doc)}`);
                                resolve(doc);
                            }
                        })
                    }else{
                        database.update(query, update, (err, numberOfUpdated, upsert) => {
                            if(err){
                                console.err(err);
                                reject(err);
                            }else{
                                console.debug(`successfully updated ${numberOfUpdated} docs with ${upsert} upserts`);
                                let res = {
                                    numberOfUpdated,
                                    upsert,
                                }
                                resolve(res);
                            }
                        });
                    }
                }
            });
        })
    }
}

function resourceDao(){
    return {
        insert: insert(resourceDb),
        find: find(resourceDb),
        findOne: findOne(resourceDb),
        update: update(resourceDb),
        remove: remove(resourceDb),
        findOrUpdate: findOrUpdate(resourceDb),
    }
}

function providerDao(){
    return {
        insert: insert(providerDb),
        find: find(providerDb),
        findOne: findOne(providerDb),
        update: update(providerDb),
        remove: remove(providerDb),
        findOrUpdate: findOrUpdate(providerDb),
    }
}

function sliceDao(){
    return {
        insert: insert(sliceDb),
        find: find(sliceDb),
        findOne: findOne(sliceDb),
        update: update(sliceDb),
        remove: remove(sliceDb),
        findOrUpdate: findOrUpdate(sliceDb),
    }
}

module.exports = {
    resourceDao,
    sliceDao,
    providerDao,
}

