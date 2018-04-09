exports.command = 'create <sliceId>'
exports.desc = 'creates a slice identified by <sliceId>'
exports.builder = {}
exports.handler = function (argv) {
    console.log(`created slice ${argv.sliceId}`)     
}