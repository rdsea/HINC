exports.command = 'intop <command>'
exports.desc = 'Check the interoperability of slices or get interoperability recommendations'
exports.builder = function (yargs) {
  return yargs.commandDir('intop_cmds')
}
exports.handler = function (argv) {}