package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.messagehandlers;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.model.LocalMS;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.LocalMSRepository;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.ProviderRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.ResourceProvider;

import java.io.IOException;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Component
public class HandleDeliverProviders extends HINCMessageHandler {
    private final ProviderRepository providerRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public HandleDeliverProviders(ProviderRepository providerRepository, MongoTemplate mongoTemplate) {
        super(HINCMessageType.DELIVER_PROVIDERS);
        this.providerRepository = providerRepository;
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


            List<ResourceProvider> providers = objectMapper.readValue(msg.getPayload(), new TypeReference<List<ResourceProvider>>(){});
            localMS.setResourceProviders(providers);
            providerRepository.saveAll(providers);

            //("group").is(group).and
            mongoTemplate.upsert(query(where("id").is(id)),
                    update("resourceProviders", providers), LocalMS.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
