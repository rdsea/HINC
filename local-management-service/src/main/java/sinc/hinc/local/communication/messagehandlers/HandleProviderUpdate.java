package sinc.hinc.local.communication.messagehandlers;

import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.ResourceProvider;
import sinc.hinc.common.model.capabilities.ControlPoint;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.io.IOException;
@Component
public class HandleProviderUpdate extends HINCMessageHandler {

    public HandleProviderUpdate() {
        super(HINCMessageType.UPDATE_PROVIDER);
    }

    @Override
    protected HincMessage doHandle(HincMessage hincMessage) {
        try {
            ResourceProvider provider = objectMapper.readValue(hincMessage.getPayload(), ResourceProvider.class);
            AbstractDAO<ResourceProvider> resourceProviderDAO = new AbstractDAO<>(ResourceProvider.class);
            logger.info(hincMessage.getPayload());

            resourceProviderDAO.save(provider);
            logger.info("successfully saved resource provider");
        } catch (IOException e) {
            logger.error("failed to serialize message payload for " + this.messageType);
            e.printStackTrace();
        }
        return null;
    }
}
