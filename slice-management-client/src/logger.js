let config = require('./config');

let colors={
    Reset: "\x1b[0m",
    Red: "\x1b[31m",
    Green: "\x1b[32m",
    Yellow: "\x1b[33m"
};

let errorLog = console.error;
let warnLog = console.warn;
let infoLog = console.info;

// orange output for WARN
console.warn= function(args)
{
    let copyArgs = Array.prototype.slice.call(arguments);
    copyArgs.unshift(colors.Yellow);
    copyArgs.push(colors.Reset);
    warnLog.apply(null,copyArgs);
};

// red output for ERROR
console.err= function(args)
{
    let copyArgs = Array.prototype.slice.call(arguments);
    copyArgs.unshift(colors.Red);
    copyArgs.push(colors.Reset);
    errorLog.apply(null,copyArgs);
};

// blue output for INFO
console.info= function(args)
{
    var copyArgs = Array.prototype.slice.call(arguments);
    copyArgs.unshift(colors.Green);
    copyArgs.push(colors.Reset);
    infoLog.apply(null,copyArgs);
};

// disable console.debug for production
if(config.production) console.debug = () => {};