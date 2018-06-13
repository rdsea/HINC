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
    public ResponseEntity<List<SoftwareArtefact>> getSoftwareArtefact(@RequestParam(required = false, defaultValue = "0")Integer limit) {

        List<SoftwareArtefact> softwareArtefactList = new ArrayList<>();
        try {
            softwareArtefactList = softwareArtefactService.querySoftwareArtefact(limit);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(softwareArtefactList, HttpStatus.OK);
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity putMetadata(@PathVariable String id, @RequestBody JsonNode metadata){
        SoftwareArtefact softwareArtefact = softwareArtefactService.putMetadata(id, metadata);

        if(softwareArtefact == null){
            return new ResponseEntity<>("SoftwareArtefact with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareArtefact, HttpStatus.OK);
    }
}
