exports.command = 'search <path>'
exports.desc = 'runs a search for resources matching the query in the file <path>'
exports.builder = {}
exports.handler = function (argv) {
    console.log(`searching resources, query located in the json file ${argv.path}`)     
}