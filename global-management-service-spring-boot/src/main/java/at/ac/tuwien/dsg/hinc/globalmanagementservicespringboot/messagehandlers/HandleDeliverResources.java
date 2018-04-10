package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.messagehandlers;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.model.LocalMS;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.LocalMSRepository;
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
    private final LocalMSRepository localMSRepository;

    @Autowired
    public HandleDeliverResources(ResourceRepository resourceRepository, LocalMSRepository localMSRepository) {
        super(HINCMessageType.DELIVER_RESOURCES);
        this.resourceRepository = resourceRepository;
        this.localMSRepository = localMSRepository;
    }

    @Override
    protected void doHandle(HincMessage msg) {
        try {

            String group = msg.getReply().getExchange();
            String id = msg.getSenderID();

            LocalMS localMS = new LocalMS();
            localMS.setId(id);
            localMS.setGroup(group);

            List<Resource> resources = objectMapper.readValue(msg.getPayload(), new TypeReference<List<Resource>>(){});
            localMS.setResources(resources);
            resourceRepository.saveAll(resources);
            localMSRepository.save(localMS);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
