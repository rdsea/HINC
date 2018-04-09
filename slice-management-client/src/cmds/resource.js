exports.command = 'resource <command>'
exports.desc = 'Manage set of available resources'
exports.builder = function (yargs) {
  return yargs.commandDir('resource_cmds')
}
exports.handler = function (argv) {}