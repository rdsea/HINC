package at.ac.tuwien.dsg.hinc.softwareartefactservice.services;

import at.ac.tuwien.dsg.hinc.softwareartefactservice.repository.SoftwareArtefactRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sinc.hinc.common.model.SoftwareArtefact;

import java.util.List;

@Component
public class SoftwareArtefactService {

    private final SoftwareArtefactRepository softwareArtefactRepository;


    @Autowired
    public SoftwareArtefactService(SoftwareArtefactRepository softwareArtefactRepository) {
        this.softwareArtefactRepository = softwareArtefactRepository;
    }

    public SoftwareArtefact createSoftwareArtefact(SoftwareArtefact toCreate){
        return softwareArtefactRepository.save(toCreate);
    }

    public SoftwareArtefact getSoftwareArtefact(String uuid){
        return softwareArtefactRepository.read(uuid);
    }

    public SoftwareArtefact deleteSoftwareArtefact(String uuid){
        return softwareArtefactRepository.delete(uuid);
    }

    public List<SoftwareArtefact> getSoftwareArtefacts(int limit) throws JsonProcessingException {
        return softwareArtefactRepository.readAll(limit);
    }

    public SoftwareArtefact putMetadata(String softwareArtefactId, JsonNode metadata){
        SoftwareArtefact softwareArtefact = softwareArtefactRepository.read(softwareArtefactId);
        if(softwareArtefact != null) {
            softwareArtefact.setMetadata(metadata);
            softwareArtefactRepository.save(softwareArtefact);
        }
        return softwareArtefact;
    }

    public List<SoftwareArtefact> queryByExample(SoftwareArtefact example){
        return softwareArtefactRepository.queryByExample(example);
    }

    public List<SoftwareArtefact> query(String query){
        return softwareArtefactRepository.query(query);
    }

}
