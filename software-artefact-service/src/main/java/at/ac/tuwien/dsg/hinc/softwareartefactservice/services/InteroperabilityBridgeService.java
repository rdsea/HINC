package at.ac.tuwien.dsg.hinc.softwareartefactservice.services;

import at.ac.tuwien.dsg.hinc.softwareartefactservice.repository.InteroperabilityBridgeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sinc.hinc.common.model.InteroperabilityBridge;

import java.util.List;

@Component
public class InteroperabilityBridgeService {

    private final InteroperabilityBridgeRepository interoperabilityBridgeRepository;


    @Autowired
    public InteroperabilityBridgeService(InteroperabilityBridgeRepository interoperabilityBridgeRepository) {
        this.interoperabilityBridgeRepository = interoperabilityBridgeRepository;
    }

    public InteroperabilityBridge createInteroperabilityBridge(InteroperabilityBridge toCreate){
        return interoperabilityBridgeRepository.save(toCreate);
    }

    public InteroperabilityBridge getInteroperabilityBridge(String uuid){
        return interoperabilityBridgeRepository.read(uuid);
    }

    public InteroperabilityBridge deleteInteroperabilityBridge(String uuid){
        return interoperabilityBridgeRepository.delete(uuid);
    }

    public List<InteroperabilityBridge> getInteroperabilityBridges(int limit) throws JsonProcessingException {
        return interoperabilityBridgeRepository.readAll(limit);
    }

    public InteroperabilityBridge putMetadata(String interoperabilityBridgeId, JsonNode metadata){
        InteroperabilityBridge interoperabilityBridge = interoperabilityBridgeRepository.read(interoperabilityBridgeId);
        if(interoperabilityBridge != null) {
            interoperabilityBridge.setMetadata(metadata);
            interoperabilityBridgeRepository.save(interoperabilityBridge);
        }
        return interoperabilityBridge;
    }

    public List<InteroperabilityBridge> queryByExample(InteroperabilityBridge example){
        return interoperabilityBridgeRepository.queryByExample(example);
    }

    public List<InteroperabilityBridge> query(String query){
        return interoperabilityBridgeRepository.query(query);
    }

}