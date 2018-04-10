package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.messagehandlers;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.ProviderRepository;
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
    protected void doHandle(HincMessage msg) {

        try {
            List<ResourceProvider> providers = objectMapper.readValue(msg.getPayload(), new TypeReference<List<ResourceProvider>>(){});
            providerRepository.saveAll(providers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
