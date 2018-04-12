package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.messagehandlers;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.model.LocalMS;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.LocalMSRepository;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.ResourceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;

import java.io.IOException;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Component
public class HandleDeliverResources extends HINCMessageHandler{

    private final ResourceRepository resourceRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public HandleDeliverResources(ResourceRepository resourceRepository, MongoTemplate mongoTemplate) {
        super(HINCMessageType.DELIVER_RESOURCES);
        this.resourceRepository = resourceRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    protected void doHandle(HincMessage msg) {
        try {

            //String group = msg.getReply().getExchange();
            String id = msg.getSenderID();

            LocalMS localMS = new LocalMS();
            localMS.setId(id);
            //localMS.setGroup(group);

            List<Resource> resources = objectMapper.readValue(msg.getPayload(), new TypeReference<List<Resource>>(){});
            localMS.setResources(resources);
            resourceRepository.saveAll(resources);


            //"group").is(group).and
            mongoTemplate.upsert(query(where("id").is(id)),
                    update("resources", resources), LocalMS.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
