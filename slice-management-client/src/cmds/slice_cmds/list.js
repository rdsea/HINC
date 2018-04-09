exports.command = 'list [limit]'
exports.desc = 'list [limit] available slices'
exports.builder = {}
exports.handler = function (argv) {
    return new Promise((resolve, reject) => {
        console.log(`listing ${argv.limit || 'all'} slices`)
    })  
}