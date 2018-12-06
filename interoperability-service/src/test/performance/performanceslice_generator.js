const fs = require("fs");
const path = require("path");
const slice_util = require("../../main/util/slice_util");
const builder = require("../testdata/testslice_builder_util");


clearResults();


for(let nodenumber = 1; nodenumber<=10; nodenumber=nodenumber*10){
    for(let metadatanumber = 1; metadatanumber<=10; metadatanumber=metadatanumber*10){
        let slice1 = direct_instance("direct_instance", nodenumber, metadatanumber, metadata_parameters_OK());
        let slice2 = direct_instance("direct_instance", nodenumber,metadatanumber, metadata_parameters_ERROR());
        let slice3 = indirect_instance("indirect_instance", nodenumber,metadatanumber, metadata_parameters_OK());
        let slice4 = indirect_instance("indirect_instance", nodenumber,metadatanumber, metadata_parameters_ERROR());

        create_file_from_slice(slice1);
        create_file_from_slice(slice2);
        create_file_from_slice(slice3);
        create_file_from_slice(slice4);
    }
}



function create_file_from_slice(slice){
    fs.writeFileSync(path.join(__dirname, `results/${slice.sliceId}.json`), JSON.stringify(slice, null, 2));
}

function clearResults(){
    if(fs.existsSync('results')){
        var deleteFolderRecursive = function(path) {
            if (fs.existsSync(path)) {
                fs.readdirSync(path).forEach(function(file, index){
                    var curPath = path + "/" + file;
                    if (fs.lstatSync(curPath).isDirectory()) { // recurse
                        deleteFolderRecursive(curPath);
                    } else { // delete file
                        fs.unlinkSync(curPath);
                    }
                });
                fs.rmdirSync(path);
            }
        };

        deleteFolderRecursive('results');
    }

    fs.mkdirSync('results');
}

function metadata_parameters_OK(){
    return copy({
        input_format : builder.jsonFormat(),
        performance_metadata_input_value : true,
        output_format : builder.jsonFormat(),
        performance_metadata_output_value : true,
        setname:"OK"
    })
}

function metadata_parameters_ERROR(){
    return copy({
        input_format : builder.csvFormat(),
        performance_metadata_input_value : false,
        output_format : builder.jsonFormat(),
        performance_metadata_output_value : true,
        setname:"ERROR"
    })
}

function direct_instance(basename, number_nodes, number_performance_metadata, metadata_parameters){
    return generate_direct_instance(basename, number_nodes, number_performance_metadata, metadata_parameters);
}


function indirect_instance(basename, number_nodes, number_performance_metadata, metadata_parameters){
    return generate_indirect_instance(basename, number_nodes, number_performance_metadata, metadata_parameters);
}


function direct_performance_metadata_only_OK(number_performance_metadata) {
    return direct_instance_OK(2, number_performance_metadata);
}

function direct_performance_metadata_only_ERROR(number_performance_metadata) {
    return direct_instance_ERROR(2, number_performance_metadata);
}

function indirect_performance_metadata_only_OK(number_performance_metadata) {
    return indirect_instance_OK(1, number_performance_metadata);
}

function indirect_performance_metadata_only_ERROR(number_performance_metadata) {
    return indirect_instance_ERROR(1, number_performance_metadata);
}




function generate_direct_instance(basename, number_nodes, number_performance_metadata, metadata_parameters){
    let input_format = metadata_parameters.input_format;
    let input_value = metadata_parameters.performance_metadata_input_value;
    let output_format = metadata_parameters.output_format;
    let output_value = metadata_parameters.performance_metadata_output_value;

    let slice = builder.empty_slice(`${basename}_${metadata_parameters.setname}_${number_nodes}_${number_performance_metadata}`);

    for(let i = 0; i<=number_nodes; i++){
        let component = create_direct_metadata_component(`component_${i}`, number_performance_metadata,output_value, output_format, input_value, input_format);
        slice_util.sliceAddResource(slice, component, component.name);
    }
    for(let i = 0; i<number_nodes; i++){
        slice_util.sliceConnectById(slice,`component_${i}`,`component_${i+1}`, `con_${i}_to_${i+1}`);
    }
    return copy(slice);
}


function generate_indirect_instance(basename, number_nodes, number_performance_metadata, metadata_parameters){
    let input_format = metadata_parameters.input_format;
    let input_value = metadata_parameters.performance_metadata_input_value;
    let output_format = metadata_parameters.output_format;
    let output_value = metadata_parameters.performance_metadata_output_value;

    let slice = builder.empty_slice(`${basename}_${metadata_parameters.setname}_${number_nodes}_${number_performance_metadata}`);

    let source = create_indirect_metadata_component("source", number_performance_metadata, output_value, output_format, input_value, input_format);
    let broker = builder.mqttBroker("broker", builder.mqttProtocol());

    slice_util.sliceAddResource(slice, source, source.name);
    slice_util.sliceAddResource(slice, broker, broker.name);
    slice_util.sliceConnectById(slice, "source", "broker", "source_to_broker");

    for(let i = 0; i<number_nodes; i++){
        let component = create_indirect_metadata_component(`dest_${i+1}`, number_performance_metadata,output_value, output_format, input_value, input_format);
        slice_util.sliceAddResource(slice, component, component.name);
    }
    for(let i = 0; i<number_nodes; i++){
        slice_util.sliceConnectById(slice,"broker",`dest_${i+1}`, `broker_to_dest${i+1}`);
    }
    return copy(slice);
}


function copy(value){
    return JSON.parse(JSON.stringify(value));
}

function create_direct_metadata_component(name, number_performance_metadata, output_value, output_format, input_value, input_format){
    let component = builder.source(name,"push", builder.httpProtocol(), output_format);
    component.metadata.outputs[0].performance_direct = {count:number_performance_metadata};

    for(let i = 0; i<number_performance_metadata; i++){
        component.metadata.outputs[0].performance_direct[`direct_performance_${i}`] = output_value;
    }
    component.metadata.inputs = builder.dest(name,"push", builder.httpProtocol(), input_format).metadata.inputs;
    component.metadata.inputs[0].performance_direct = {count:number_performance_metadata};
    for(let i = 0; i<number_performance_metadata; i++){
        component.metadata.inputs[0].performance_direct[`direct_performance_${i}`] = input_value;
    }
    return copy(component);
}

function create_indirect_metadata_component(name, number_performance_metadata, output_value, output_format, input_value, input_format){
    let component = builder.source(name,"push", builder.mqttProtocol(), output_format);
    component.metadata.outputs[0].performance_indirect = {count:number_performance_metadata};

    for(let i = 0; i<number_performance_metadata; i++){
        component.metadata.outputs[0].performance_indirect[`indirect_performance_${i}`] = output_value;
    }
    component.metadata.inputs = builder.dest(name,"push", builder.mqttProtocol(), input_format).metadata.inputs;
    component.metadata.inputs[0].performance_indirect = {count:number_performance_metadata};
    for(let i = 0; i<number_performance_metadata; i++){
        component.metadata.inputs[0].performance_indirect[`indirect_performance_${i}`] = input_value;
    }
    return copy(component);
}