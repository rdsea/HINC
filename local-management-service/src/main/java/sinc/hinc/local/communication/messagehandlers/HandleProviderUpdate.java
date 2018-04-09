package sinc.hinc.local.communication.messagehandlers;

import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.ResourceProvider;
import sinc.hinc.common.model.capabilities.ControlPoint;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import javax.naming.ldap.Control;
import java.io.IOException;
import java.util.Arrays;

public class HandleProviderUpdate extends HINCMessageHandler {

    public HandleProviderUpdate() {
        super(HINCMessageType.UPDATE_PROVIDER);
    }

    @Override
    protected void doHandle(HincMessage hincMessage) {
        try {
            ResourceProvider provider = objectMapper.readValue(hincMessage.getPayload(), ResourceProvider.class);
            AbstractDAO<ResourceProvider> resourceProviderDAO = new AbstractDAO<>(ResourceProvider.class);

            for(ControlPoint controlPoint: provider.getManagementPoints()){
                controlPoint.setUuid(controlPoint.getName());
            }

            resourceProviderDAO.save(provider);
            logger.info("successfully saved resource provider");
        } catch (IOException e) {
            logger.error("failed to serialize message payload for " + this.messageType);
            e.printStackTrace();
        }
    }
}
