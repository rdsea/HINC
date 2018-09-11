package sinc.hinc.local.communication.messagehandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;
import sinc.hinc.local.repository.ResourceRepository;
import sinc.hinc.repository.DAO.AbstractDAO;

import java.io.IOException;
import java.util.Arrays;
@Component
public class HandleResourcesUpdate extends HINCMessageHandler {
    @Autowired
    private ResourceRepository resourceRepository;

    public HandleResourcesUpdate() {
        super(HINCMessageType.UPDATE_RESOURCES);
    }

    @Override
    protected HincMessage doHandle(HincMessage hincMessage) {
        try {
            Resource[] resources = objectMapper.readValue(hincMessage.getPayload(), Resource[].class);

            resourceRepository.saveAll(Arrays.asList(resources));
            logger.info("successfully saved "+resources.length+" resources");
        } catch (IOException e) {
            logger.error("failed to serialize message payload for " + this.messageType);
            e.printStackTrace();
        }
        return null;
    }
}
