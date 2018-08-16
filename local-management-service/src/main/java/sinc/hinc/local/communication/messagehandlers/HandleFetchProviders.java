package sinc.hinc.local.communication.messagehandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.ResourceProvider;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.repository.ProviderRepository;
import sinc.hinc.local.repository.ResourceRepository;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.util.List;
@Component
public class HandleFetchProviders extends HINCMessageHandler {

    @Autowired
    private ProviderRepository providerRepository;

    public HandleFetchProviders() {
        super(HINCMessageType.FETCH_PROVIDERS);
    }

    @Override
    protected HincMessage doHandle(HincMessage hincMessage) {

        List<ResourceProvider> providers = providerRepository.readAll();
        try {
            String payload = this.objectMapper.writeValueAsString(providers);
            HincMessage message = new HincMessage(
                    HINCMessageType.DELIVER_PROVIDERS,
                    HincConfiguration.getMyUUID(),
                    payload
            );

            return message;
        } catch (JsonProcessingException e) {
            logger.error("failed to serialize payload for "+this.messageType);
            e.printStackTrace();
        }
        return null;
    }
}
