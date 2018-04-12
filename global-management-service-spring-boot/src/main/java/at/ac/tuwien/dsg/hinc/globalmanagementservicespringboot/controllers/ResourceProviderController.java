package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.controllers;


import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.services.ResourceProviderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.ResourceProvider;
import sinc.hinc.common.model.payloads.Control;
import sinc.hinc.common.model.payloads.ControlResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/resourceproviders")
public class ResourceProviderController {

    private final ResourceProviderService resourceProviderService;

    @Autowired
    public ResourceProviderController(ResourceProviderService resourceProviderService) {
        this.resourceProviderService = resourceProviderService;
    }

    @GetMapping
    public ResponseEntity<List<ResourceProvider>> getResources(@RequestParam(required = false, defaultValue = "3000")Integer timeout,
                                                       //@RequestParam(required = false, defaultValue = "")String id,
                                                       //@RequestParam(required = false, defaultValue = "")String group,
                                                       @RequestParam(required = false, defaultValue = "0")Integer limit){
                                                       //@RequestParam(required = false, defaultValue = "false")Boolean rescan) {

        List<ResourceProvider> resourceProviderList = new ArrayList<>();
        try {
            resourceProviderList = resourceProviderService.queryResourceProviders(timeout,limit);
            return new ResponseEntity<>(resourceProviderList, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resourceProviderList, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/{providerId}/controlpoints/{controlPointId}")
    public ResponseEntity<ControlResult> postToManagementPointOfProvider(@PathVariable String providerId,
                                                                      @PathVariable String controlPointId,
                                                                      @RequestBody JsonNode parameters){

        try {
            return new ResponseEntity<>(resourceProviderService.sendControl(providerId,controlPointId,parameters), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ControlResult errorResult = new ControlResult();
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/control")
    public ResponseEntity<ControlResult> postToManagementPointOfProvider(@RequestBody Control control){
        try {
            return new ResponseEntity<>(resourceProviderService.sendControl(control), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ControlResult errorResult = new ControlResult();
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*GET /resourceproviders?type=&group=&id=&limit=&rescan=true&timeout=
    GET /resourceproviders/{id}/managementpoints
    POST /resourceproviders/{id}/managementpoints/{id}   Parameter:parameter
    GET /resourceproviders/{id}/resources
    GET /resourceproviders/{id}/resources/{id}/datapoints
    GET /resourceproviders/{id}/resources/{id}/controlpoints
    POST /resourceproviders/{id}/resources/{id}/controlpoints/{id}   Parameter:parameter*/
}
