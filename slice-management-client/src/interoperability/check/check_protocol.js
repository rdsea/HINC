exports.checkProtocols = function (connection, problems) {
    source = connection.source;
    target = connection.target;

    console.log("check " + connection.connectionId);
    console.log("source.metadata.output[0].protocol " + source.metadata.output[0].protocol);
    console.log("target.metadata.input[0].protocol " + target.metadata.input[0].protocol);
    if (source.metadata.output[0].protocol !== target.metadata.input[0].protocol) {
        problems.push(source.metadata.id + " </> " + target.metadata.id );
    }
};