package at.ac.tuwien.dsg.hinc.softwareartefactservice.services;

import at.ac.tuwien.dsg.hinc.softwareartefactservice.repository.SoftwareArtefactRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.SoftwareArtefact;

import java.util.List;

@Component
public class SoftwareArtefactService {

    private final SoftwareArtefactRepository softwareArtefactRepository;

    private final ObjectMapper objectMapper;


    @Autowired
    public SoftwareArtefactService(SoftwareArtefactRepository softwareArtefactRepository, ObjectMapper objectMapper) {
        this.softwareArtefactRepository = softwareArtefactRepository;
        this.objectMapper = objectMapper;
    }

    public List<SoftwareArtefact> querySoftwareArtefact(int limit) throws JsonProcessingException {
        //TODO temporary queue
        return null;
    }

    public SoftwareArtefact putMetadata(String softwareArtefactId, JsonNode metadata){
        SoftwareArtefact softwareArtefact = softwareArtefactRepository.read(softwareArtefactId);
        if(softwareArtefact != null) {
            softwareArtefact.setMetadata(metadata);
            softwareArtefactRepository.save(softwareArtefact);
        }
        return softwareArtefact;
    }
}
