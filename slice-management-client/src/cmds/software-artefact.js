exports.command = 'artefact <command>'
exports.desc = 'Manage software artefacts'
exports.builder = function (yargs) {
  return yargs.commandDir('software_artefact_cmds')
}
exports.handler = function (argv) {}