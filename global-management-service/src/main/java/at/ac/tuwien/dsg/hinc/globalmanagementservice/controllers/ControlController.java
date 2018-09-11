package at.ac.tuwien.dsg.hinc.globalmanagementservice.controllers;

import at.ac.tuwien.dsg.hinc.globalmanagementservice.services.ControlService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sinc.hinc.common.model.Resource;

@RestController
@RequestMapping("/controls")
public class ControlController {

    @Autowired
    private ControlService controlService;

    @PostMapping("/provision")
    public ResponseEntity<Object> provisionResource(@RequestBody Resource resource){

        try {
            Resource provisionedResource = controlService.provision(resource);
            return new ResponseEntity<>(provisionedResource, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteResource(@RequestBody Resource resource){
        try{
            Resource deletedResource = controlService.delete(resource);
            return new ResponseEntity<Object>(deletedResource, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/configure")
    public ResponseEntity<Object> configureResource(@RequestBody Resource resource){
        try{
            Resource configuredResource = controlService.configure(resource);
            return new ResponseEntity<Object>(configuredResource, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logs")
    public ResponseEntity<Object> getLogs(@RequestBody Resource resource){
        try{
            JsonNode logs = controlService.getLogs(resource);
            return new ResponseEntity<Object>(logs, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
