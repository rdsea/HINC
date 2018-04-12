package sinc.hinc.local.communication.messagehandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.communication.AdaptorCommunicationManager;
import sinc.hinc.local.communication.LocalCommunicationManager;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.util.List;

public class HandleFetchResources extends HINCMessageHandler {
    public HandleFetchResources() {
        super(HINCMessageType.FETCH_RESOURCES);
    }

    @Override
    protected void doHandle(HincMessage hincMessage) {
        AbstractDAO<Resource> resourceAbstractDAO = new AbstractDAO<>(Resource.class);

        List<Resource> resources = resourceAbstractDAO.readAll();
        logger.info(resources.size()+ " resources fetched");
        try {
            String payload = this.objectMapper.writeValueAsString(resources);
            HincMessage message = new HincMessage(
                    HINCMessageType.DELIVER_RESOURCES,
                    HincConfiguration.getMyUUID(),
                    payload
            );

            message.setDestination(hincMessage.getReply().getExchange(), hincMessage.getReply().getRoutingKey());
            message.setReply(AdaptorCommunicationManager.getInstance().getExchange(), "");
            logger.info("sending reply " + HINCMessageType.DELIVER_RESOURCES.name() + " to "+ hincMessage.getReply().getRoutingKey());
            AdaptorCommunicationManager.getInstance().sendMessage(message);
        } catch (JsonProcessingException e) {
            logger.error("failed to serialize payload for "+this.messageType);
            e.printStackTrace();
        }
    }
}
