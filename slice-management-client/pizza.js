#!/usr/bin/env node

const pkg = require('./package.json');
const figlet = require('figlet');
const yargs = require('yargs');
const amqpTools = require('./src/amqpTools.js');
const logger = require('./src/logger.js');
const config = require('./src/config.js');

let pizza = `
            88
            ""
8b,dPPYba,  88 888888888 888888888 ,adPPYYba,
88P     "8a 88      a8P"      a8P" ""      Y8
88       d8 88   ,d8P'     ,d8P'   ,adPPPPP88
88b,   ,a8" 88 ,d8"      ,d8"      88,    ,88
88'YbbdP"'  88 888888888 888888888  "8bbdP"Y8
88
88

`

if(config.hasConfig()){
    yargs
    .commandDir('./src/cmds')
    .demandCommand()
    .help()
    .version(pkg.version)
    .argv;
}else{
    config.setConfig();
}
