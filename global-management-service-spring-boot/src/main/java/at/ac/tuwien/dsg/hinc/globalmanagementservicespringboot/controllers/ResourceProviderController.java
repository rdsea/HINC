package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.controllers;


import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.services.ResourceProviderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.ResourceProvider;

import java.util.List;

@RestController("/resourceproviders")
public class ResourceProviderController {

    private final ResourceProviderService resourceProviderService;

    @Autowired
    public ResourceProviderController(ResourceProviderService resourceProviderService) {
        this.resourceProviderService = resourceProviderService;
    }

    @GetMapping
    public ResponseEntity<List<ResourceProvider>> getResources(@RequestParam(required = false, defaultValue = "0")Integer timeout,
                                                       @RequestParam(required = false, defaultValue = "")String id,
                                                       @RequestParam(required = false, defaultValue = "")String group,
                                                       @RequestParam(required = false, defaultValue = "0")Integer limit,
                                                       @RequestParam(required = false, defaultValue = "false")Boolean rescan) {

        List<ResourceProvider> resourceProviderList = null;
        try {
            resourceProviderList = resourceProviderService.queryResourceProviders(timeout,id,group,limit,rescan);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resourceProviderList, HttpStatus.OK);
    }

    /*GET /resourceproviders?type=&group=&id=&limit=&rescan=true&timeout=
    GET /resourceproviders/{id}/managementpoints
    POST /resourceproviders/{id}/managementpoints/{id}   Parameter:parameter
    GET /resourceproviders/{id}/resources
    GET /resourceproviders/{id}/resources/{id}/datapoints
    GET /resourceproviders/{id}/resources/{id}/controlpoints
    POST /resourceproviders/{id}/resources/{id}/controlpoints/{id}   Parameter:parameter*/
}
