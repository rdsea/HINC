const db = require('../../data/db');
const Table = require('cli-table');
const moment = require('moment');
const prompt = require('inquirer').createPromptModule();

exports.command = 'intop <sliceId>'
exports.desc = 'perform an interoperability check on slice <sliceId>'
exports.builder = {}

exports.handler = function (argv) {

    console.log("not implemented yet");
};