package sinc.hinc.local.communication.messagehandlers;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;
import sinc.hinc.local.plugin.AdaptorManager;

import java.io.IOException;

public class HandleConfigure extends HINCMessageHandler {
    @Value("${hinc.local.group}")
    private String group;
    @Value("${hinc.local.id}")
    private String id;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange adaptorInputExchange;
    @Autowired
    private DirectExchange adaptorOutputUnicastExchange;
    @Autowired
    private AdaptorManager adaptorManager;

    public HandleConfigure(HINCMessageType messageType) {
        super(HINCMessageType.CONFIGURE);
    }

    @Override
    protected HincMessage doHandle(HincMessage msg) {
        logger.debug("received " + msg.toString());
        Resource resource = null;
        try {
            resource = objectMapper.readValue(msg.getPayload(), Resource.class);
        } catch (IOException e) {
            logger.error("failed to marshall resource");
            e.printStackTrace();
        }

        try {
            Resource configuredResource = adaptorManager.configureResource(resource.getProviderUuid(), resource);
            HincMessage replyMessage = new HincMessage();

            replyMessage.setMsgType(HINCMessageType.CONFIGURE_RESULT);
            replyMessage.setUuid(group + "." + id);

            replyMessage.setSenderID(id);
            replyMessage.setPayload(objectMapper.writeValueAsString(configuredResource));

            return replyMessage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
