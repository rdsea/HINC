package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.messagehandlers;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.ResourceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;

import java.io.IOException;
import java.util.List;

@Component
public class HandleDeliverResources extends HINCMessageHandler{

    private final ResourceRepository resourceRepository;

    @Autowired
    public HandleDeliverResources(ResourceRepository resourceRepository) {
        super(HINCMessageType.DELIVER_RESOURCES);
        this.resourceRepository = resourceRepository;
    }

    @Override
    protected void doHandle(HincMessage msg) {
        try {
            List<Resource> resources = objectMapper.readValue(msg.getPayload(), new TypeReference<List<Resource>>(){});
            resourceRepository.saveAll(resources);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
