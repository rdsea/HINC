package sinc.hinc.local.communication.messagehandlers;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.payloads.ControlResult;
import sinc.hinc.local.plugin.Adaptor;
import sinc.hinc.local.plugin.AdaptorManager;

import java.io.IOException;

@Component
@Configuration
public class HandleProvision extends HINCMessageHandler {
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

    public HandleProvision() {
        super(HINCMessageType.PROVISION);
    }


    @Override
    protected HincMessage doHandle(HincMessage msg){
        logger.debug("received " + msg.toString());
        Resource resource = null;
        try {
            resource = objectMapper.readValue(msg.getPayload(), Resource.class);
        } catch (IOException e) {
            logger.error("failed to marshall resource");
            e.printStackTrace();
        }

        try {
            Resource provisionedResource = adaptorManager.provisionResource(resource.getProviderUuid(), resource);
            HincMessage replyMessage = new HincMessage();

            replyMessage.setMsgType(HINCMessageType.PROVISION_RESULT);
            replyMessage.setUuid(group + "." + id);

            replyMessage.setSenderID(id);
            replyMessage.setPayload(objectMapper.writeValueAsString(provisionedResource));

            return replyMessage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

