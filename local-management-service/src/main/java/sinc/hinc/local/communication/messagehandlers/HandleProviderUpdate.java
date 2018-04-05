package sinc.hinc.local.communication.messagehandlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.model.ResourceProvider;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.io.IOException;
import java.util.Arrays;

public class HandleProviderUpdate extends HINCMessageHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private ObjectMapper objectMapper;

    public HandleProviderUpdate(HINCMessageType messageType) {
        super(messageType);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    protected void doHandle(HincMessage hincMessage) {
        try {
            ResourceProvider resources = objectMapper.readValue(hincMessage.getPayload(), ResourceProvider.class);
            AbstractDAO<ResourceProvider> resourceDAO = new AbstractDAO<>(ResourceProvider.class);
            resourceDAO.saveAll(Arrays.asList(resources));
            logger.info("successfully saved resource provider");
        } catch (IOException e) {
            logger.error("failed to serialize message payload for " + this.messageType);
            e.printStackTrace();
        }
    }
}
