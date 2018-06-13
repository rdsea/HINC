package at.ac.tuwien.dsg.hinc.softwareartefactservice.controllers;

import at.ac.tuwien.dsg.hinc.softwareartefactservice.services.ResourceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.capabilities.ControlPoint;
import sinc.hinc.common.model.capabilities.DataPoint;
import sinc.hinc.common.model.payloads.ControlResult;

import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }


    @GetMapping
    public ResponseEntity<List<Resource>> getResources(@RequestParam(required = false, defaultValue = "3000")Integer timeout,
                                                       //@RequestParam(required = false, defaultValue = "")String id,
                                                       //@RequestParam(required = false, defaultValue = "")String group,
                                                       @RequestParam(required = false, defaultValue = "0")Integer limit
                                                       //@RequestParam(required = false, defaultValue = "false")Boolean rescan
                                                        ) {

        List<Resource> resourceList = null;
        try {
            resourceList = resourceService.queryResources(timeout, limit);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resourceList, HttpStatus.OK);
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity putMetadata(@PathVariable String id, @RequestBody JsonNode metadata){
        Resource resource = resourceService.putMetadata(id, metadata);

        if(resource == null){
            return new ResponseEntity<>("Resource with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
