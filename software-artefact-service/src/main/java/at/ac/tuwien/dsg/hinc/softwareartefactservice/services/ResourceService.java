package at.ac.tuwien.dsg.hinc.softwareartefactservice.services;

import at.ac.tuwien.dsg.hinc.softwareartefactservice.repository.ResourceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sinc.hinc.common.model.Resource;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceService {

    private final ResourceRepository resourceRepository;

    private final ObjectMapper objectMapper;


    @Autowired
    public ResourceService(ResourceRepository resourceRepository, ObjectMapper objectMapper) {
        this.resourceRepository = resourceRepository;
        this.objectMapper = objectMapper;
    }

    public List<Resource> queryResources(int timeout, int limit) throws JsonProcessingException {
        //TODO temporary queue
        return null;
    }

    public Resource putMetadata(String resourceId, JsonNode metadata){
        Resource resource = resourceRepository.read(resourceId);
        if(resource != null) {
            resource.setMetadata(metadata);
            resourceRepository.save(resource);
        }

        return resource;
    }
}
