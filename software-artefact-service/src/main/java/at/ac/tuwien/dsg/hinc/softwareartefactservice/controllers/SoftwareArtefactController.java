package at.ac.tuwien.dsg.hinc.softwareartefactservice.controllers;

import at.ac.tuwien.dsg.hinc.softwareartefactservice.services.SoftwareArtefactService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sinc.hinc.common.model.SoftwareArtefact;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/softwareartefacts")
public class SoftwareArtefactController {

    private final SoftwareArtefactService softwareArtefactService;

    @Autowired
    public SoftwareArtefactController(SoftwareArtefactService softwareArtefactService) {
        this.softwareArtefactService = softwareArtefactService;
    }


    @PutMapping
    public ResponseEntity<SoftwareArtefact> createSoftwareArtefact(@RequestBody SoftwareArtefact softwareArtefact) {

        SoftwareArtefact result = softwareArtefactService.createSoftwareArtefact(softwareArtefact);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SoftwareArtefact>> getSoftwareArtefacts(@RequestParam(required = false, defaultValue = "0")Integer limit) {

        List<SoftwareArtefact> softwareArtefactList = new ArrayList<>();
        try {
            softwareArtefactList = softwareArtefactService.getSoftwareArtefacts(limit);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(softwareArtefactList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getSoftwareArtefact(@PathVariable String id){
        SoftwareArtefact softwareArtefact = softwareArtefactService.getSoftwareArtefact(id);

        if(softwareArtefact == null){
            return new ResponseEntity<>("SoftwareArtefact with id " + id + " not found", HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(softwareArtefact, HttpStatus.OK);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSoftwareArtefact(@PathVariable String id){
        SoftwareArtefact softwareArtefact = softwareArtefactService.deleteSoftwareArtefact(id);

        if(softwareArtefact == null){
            return new ResponseEntity<>("SoftwareArtefact with id " + id + " not found", HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(softwareArtefact, HttpStatus.OK);
        }
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity putMetadata(@PathVariable String id, @RequestBody JsonNode metadata){
        SoftwareArtefact softwareArtefact = softwareArtefactService.putMetadata(id, metadata);

        if(softwareArtefact == null){
            return new ResponseEntity<>("SoftwareArtefact with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareArtefact, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<SoftwareArtefact>> querySoftwareArtefactsWithBody(@RequestBody String query) {
        return new ResponseEntity<>(softwareArtefactService.query(query), HttpStatus.OK);
    }
}
