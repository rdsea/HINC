exports.command = 'provider <command>'
exports.desc = 'Manage set of available resource providers'
exports.builder = function (yargs) {
  return yargs.commandDir('provider_cmds')
}
exports.handler = function (argv) {}