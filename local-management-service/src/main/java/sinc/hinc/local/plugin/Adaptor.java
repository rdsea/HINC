package sinc.hinc.local.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HincMessage;

import java.io.IOException;
import java.util.Map;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.payloads.Control;
import sinc.hinc.common.model.payloads.ControlResult;
import sinc.hinc.common.utils.HincConfiguration;

public class Adaptor {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private String name;

    private String adaptorInputQueue;
    private String adaptorOutputUnicastExchange;
    private String group;
    private String id;

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public Adaptor(RabbitTemplate rabbitTemplate, String adaptorOutputUnicastExchange, String adaptorInputQueue, String group, String id){
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.adaptorInputQueue = adaptorInputQueue;
        this.adaptorOutputUnicastExchange = adaptorOutputUnicastExchange;
        this.group = group;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void scanResources(){
        HincMessage message = new HincMessage(
                HINCMessageType.QUERY_RESOURCES,
                HincConfiguration.getMyUUID(),
                "");

        MessageProperties properties = new MessageProperties();
        System.out.println(adaptorInputQueue);
        properties.setReplyTo(adaptorInputQueue);
        Message msg = new Message(message.toJson().getBytes(), properties);
        rabbitTemplate.send(adaptorOutputUnicastExchange, name, msg);

    }

    public void scanResourceProvider(){
        HincMessage message = new HincMessage(
                HINCMessageType.QUERY_PROVIDER,
                HincConfiguration.getMyUUID(),
                "");

        MessageProperties properties = new MessageProperties();
        properties.setReplyTo(adaptorInputQueue);
        Message msg = new Message(message.toJson().getBytes(), properties);
        rabbitTemplate.send(adaptorOutputUnicastExchange, name, msg);

    }

    public void sendControl(Control control){

    }

    public Resource provisionResource(Resource resource) throws IOException {
        HincMessage provisionMessage = new HincMessage();

        provisionMessage.setMsgType(HINCMessageType.PROVISION);
        provisionMessage.setUuid(group + "." + id);

        provisionMessage.setSenderID(group + "." + id);
        provisionMessage.setPayload(objectMapper.writeValueAsString(resource));

        logger.info("sending provision message");
        logger.info(provisionMessage.toJson());

        Object rawReply = rabbitTemplate.convertSendAndReceive(
                adaptorOutputUnicastExchange,
                resource.getProviderUuid(),
                provisionMessage.toJson().getBytes()
        );

        HincMessage reply = null;
        String stringReply = new String(((byte[]) rawReply));

        reply = objectMapper.readValue(stringReply, HincMessage.class);

        ControlResult result = objectMapper.readValue(reply.getPayload(), ControlResult.class);
        Resource provisionedResource = objectMapper.readValue(result.getRawOutput(), Resource.class);

        return provisionedResource;
    }
}
