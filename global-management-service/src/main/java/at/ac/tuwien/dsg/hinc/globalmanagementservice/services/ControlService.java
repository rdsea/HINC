package at.ac.tuwien.dsg.hinc.globalmanagementservice.services;

import at.ac.tuwien.dsg.hinc.globalmanagementservice.repository.ProviderRepository;
import at.ac.tuwien.dsg.hinc.globalmanagementservice.repository.ResourceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;


import java.io.IOException;

@Component
public class ControlService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private FanoutExchange broadcastExchange;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private ProviderRepository providerRepository;

    @Value("${hinc.global.id}")
    private String globalId;

    public Resource provision(Resource resource) throws IOException {
        HincMessage provisionMessage = new HincMessage();

        provisionMessage.setMsgType(HINCMessageType.PROVISION);
        provisionMessage.setUuid(globalId);

        provisionMessage.setSenderID(globalId);
        provisionMessage.setPayload(objectMapper.writeValueAsString(resource));

        logger.info("sending provision message to "+broadcastExchange.getName());

        byte[] byteMessage = objectMapper.writeValueAsBytes(provisionMessage);

        Object rawReply = rabbitTemplate.convertSendAndReceive(
                broadcastExchange.getName(),
                "",
                byteMessage
        );
        logger.info("provision reply received");

        HincMessage reply = null;
        String stringReply = new String(((byte[]) rawReply));
        System.out.println(stringReply);
        reply = objectMapper.readValue(stringReply, HincMessage.class);

        Resource provisionedResource = objectMapper.readValue(reply.getPayload(), Resource.class);
        resourceRepository.save(provisionedResource);


        return provisionedResource;
    }

    public Resource delete(Resource resource) throws IOException{
        HincMessage deleteMessage = new HincMessage();
        deleteMessage.setMsgType(HINCMessageType.DELETE);
        deleteMessage.setUuid(globalId);
        deleteMessage.setSenderID(globalId);
        deleteMessage.setPayload(objectMapper.writeValueAsString(resource));

        logger.info("sending delete message to "+broadcastExchange.getName());
        Object rawReply = rabbitTemplate.convertSendAndReceive(
                broadcastExchange.getName(),
                "",
                deleteMessage.toJson().getBytes()
        );

        HincMessage reply = null;
        String stringReply = new String(((byte[]) rawReply));
        System.out.println(stringReply);
        reply = objectMapper.readValue(stringReply, HincMessage.class);

        Resource deletedResource = objectMapper.readValue(reply.getPayload(), Resource.class);

        resourceRepository.delete(deletedResource.getUuid());

        return deletedResource;

    }

    public Resource configure(Resource resource) throws IOException{
        HincMessage configureMessage = new HincMessage();
        configureMessage.setMsgType(HINCMessageType.CONFIGURE);
        configureMessage.setUuid(globalId);
        configureMessage.setSenderID(globalId);
        configureMessage.setPayload(objectMapper.writeValueAsString(resource));

        logger.info("sending configure message to "+broadcastExchange.getName());
        Object rawReply = rabbitTemplate.convertSendAndReceive(
                broadcastExchange.getName(),
                "",
                configureMessage.toJson().getBytes()
        );

        HincMessage reply = null;
        String stringReply = new String(((byte[]) rawReply));
        System.out.println(stringReply);
        reply = objectMapper.readValue(stringReply, HincMessage.class);

        Resource configuredResource = objectMapper.readValue(reply.getPayload(), Resource.class);
        resourceRepository.save(configuredResource);


        return configuredResource;
    }

    public JsonNode getLogs(Resource resource) throws IOException{
        HincMessage getLogsMessage = new HincMessage();
        getLogsMessage.setMsgType(HINCMessageType.GET_LOGS);
        getLogsMessage.setUuid(globalId);
        getLogsMessage.setSenderID(globalId);
        getLogsMessage.setPayload(objectMapper.writeValueAsString(resource));

        logger.info("sending log message to "+broadcastExchange.getName());
        Object rawReply = rabbitTemplate.convertSendAndReceive(
                broadcastExchange.getName(),
                "",
                getLogsMessage.toJson().getBytes()
        );

        HincMessage reply = null;
        String stringReply = new String(((byte[]) rawReply));
        System.out.println(stringReply);
        reply = objectMapper.readValue(stringReply, HincMessage.class);

        return objectMapper.readTree(reply.getPayload());
    }
}
