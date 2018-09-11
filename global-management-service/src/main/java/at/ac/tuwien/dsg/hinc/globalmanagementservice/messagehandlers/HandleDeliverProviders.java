package at.ac.tuwien.dsg.hinc.globalmanagementservice.messagehandlers;

import at.ac.tuwien.dsg.hinc.globalmanagementservice.model.LocalMS;
import at.ac.tuwien.dsg.hinc.globalmanagementservice.repository.ProviderRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.ResourceProvider;

import java.io.IOException;
import java.util.List;

@Component
public class HandleDeliverProviders extends HINCMessageHandler {
    private final ProviderRepository providerRepository;

    @Autowired
    public HandleDeliverProviders(ProviderRepository providerRepository) {
        super(HINCMessageType.DELIVER_PROVIDERS);
        this.providerRepository = providerRepository;
    }

    @Override
    protected HincMessage doHandle(HincMessage msg) {

        try {
            //String group = msg.getReply().getExchange();
            String id = msg.getSenderID();

            LocalMS localMS = new LocalMS();
            localMS.setId(id);
            //localMS.setGroup(group);


            List<ResourceProvider> providers = objectMapper.readValue(msg.getPayload(), new TypeReference<List<ResourceProvider>>(){});
            localMS.setResourceProviders(providers);
            providerRepository.saveAll(providers);

            //TODO handle localMS

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
