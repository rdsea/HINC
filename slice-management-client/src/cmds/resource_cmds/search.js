const db = require('../../data/db');

exports.command = 'search <path>'
exports.desc = 'runs a search for resources matching the query in the json file <path>. Only searches local database'
exports.builder = {}
exports.handler = function (argv) {
    console.log(`searching resources, query located in the json file ${argv.path}`)  
    let query = fs.readFileSync(path.join(process.cwd(), argv.file));

    return db.resourceDao().find(query).then((resources) => {
        _displayResources(resources);
    });
    
}

function _displayResources(resources){
    resources.forEach((resource, count) => {
        console.log(JSON.stringify(resource, null, 2));
        console.log('\n================================================\n')
    });    
    console.log(`retrieved ${resources.length} resources`)
}