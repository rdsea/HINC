exports.command = 'slice <command>'
exports.desc = 'Manage set of available slices'
exports.builder = function (yargs) {
  return yargs.commandDir('slice_cmds')
}
exports.handler = function (argv) {}