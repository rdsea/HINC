const resource_check = require('../adaptors/resources');
const resource_delete = require('../controls/delete');
const resource_provision = require('../controls/provision');
const assert = require('assert');


describe('test_resource.deleteResource', function(){
    describe('0 - basic delete resource information', function(){
        it('0_0_delete_resource: delete resource', function () {

          let resource = {
            "parameters":{
                "image": "nodered/node-red-docker",
                "ports": [
                    1880
                ],
                "environment": [
                    {
                        "name": "ENV_NAME_JUST_TEST",
                        "value": "variable_test"
                    }
                ],
                "files": [
                    {
                        "name": "test_name",
                        "path": "/tmp/",
                        "body": "the content of the file here"
                    }
                ]
            }
          }
          let provisioningresult =resource_provision.provision(resource);
            //provisioning
          console.log(provisioningresult);
            //check
            let result = resource_check.getItems();
            //deleted
          let deleteresult =resource_delete.deleteResource(provisioningresult);
            console.log(result);
        });
    });

});
