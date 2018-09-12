package at.ac.tuwien.dsg.hinc.softwareartefactservice.controllers;

import at.ac.tuwien.dsg.hinc.softwareartefactservice.services.InteroperabilityBridgeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sinc.hinc.common.model.InteroperabilityBridge;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/interoperabilitybridge")
public class InteroperabilityBridgeController {

    private final InteroperabilityBridgeService interoperabilityBridgeService;

    @Autowired
    public InteroperabilityBridgeController(InteroperabilityBridgeService interoperabilityBridgeService) {
        this.interoperabilityBridgeService = interoperabilityBridgeService;
    }


    @PutMapping
    public ResponseEntity<InteroperabilityBridge> createInteroperabilityBridge(@RequestBody InteroperabilityBridge interoperabilityBridge) {

        InteroperabilityBridge result = interoperabilityBridgeService.createInteroperabilityBridge(interoperabilityBridge);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<InteroperabilityBridge>> getInteroperabilityBridges(@RequestParam(required = false, defaultValue = "0")Integer limit) {

        List<InteroperabilityBridge> interoperabilityBridgeList = new ArrayList<>();
        try {
            interoperabilityBridgeList = interoperabilityBridgeService.getInteroperabilityBridges(limit);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(interoperabilityBridgeList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getInteroperabilityBridge(@PathVariable String id){
        InteroperabilityBridge interoperabilityBridge = interoperabilityBridgeService.getInteroperabilityBridge(id);

        if(interoperabilityBridge == null){
            return new ResponseEntity<>("InteroperabilityBridge with id " + id + " not found", HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(interoperabilityBridge, HttpStatus.OK);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteInteroperabilityBridge(@PathVariable String id){
        InteroperabilityBridge interoperabilityBridge = interoperabilityBridgeService.deleteInteroperabilityBridge(id);

        if(interoperabilityBridge == null){
            return new ResponseEntity<>("InteroperabilityBridge with id " + id + " not found", HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(interoperabilityBridge, HttpStatus.OK);
        }
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity putMetadata(@PathVariable String id, @RequestBody JsonNode metadata){
        InteroperabilityBridge interoperabilityBridge = interoperabilityBridgeService.putMetadata(id, metadata);

        if(interoperabilityBridge == null){
            return new ResponseEntity<>("InteroperabilityBridge with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(interoperabilityBridge, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<InteroperabilityBridge>> queryInteroperabilityBridges(@RequestParam(required = false, defaultValue = "{}")String query) {
        return new ResponseEntity<>(interoperabilityBridgeService.query(query), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<InteroperabilityBridge>> queryInteroperabilityBridgesWithBody(@RequestBody String query) {
        return new ResponseEntity<>(interoperabilityBridgeService.query(query), HttpStatus.OK);
    }
}
