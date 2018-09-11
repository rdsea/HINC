package sinc.hinc.local.communication.messagehandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.ResourceProvider;
import sinc.hinc.common.model.capabilities.ControlPoint;
import sinc.hinc.local.repository.ProviderRepository;
import sinc.hinc.repository.DAO.AbstractDAO;

import java.io.IOException;
@Component
public class HandleProviderUpdate extends HINCMessageHandler {
    @Autowired
    private ProviderRepository providerRepository;

    public HandleProviderUpdate() {
        super(HINCMessageType.UPDATE_PROVIDER);
    }

    @Override
    protected HincMessage doHandle(HincMessage hincMessage) {
        try {
            ResourceProvider provider = objectMapper.readValue(hincMessage.getPayload(), ResourceProvider.class);

            logger.info(hincMessage.getPayload());

            providerRepository.save(provider);
            logger.info("successfully saved resource provider");
        } catch (IOException e) {
            logger.error("failed to serialize message payload for " + this.messageType);
            e.printStackTrace();
        }
        return null;
    }
}
