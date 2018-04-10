package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.services;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.ResourceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;

import java.util.List;
import java.util.UUID;

@Component
public class ResourceService {

    private final RabbitTemplate rabbitTemplate;

    private final ResourceRepository resourceRepository;

    private final ObjectMapper objectMapper;

    @Value("${hinc.global.rabbitmq.input}")
    private String globalInputExchange;

    @Value("${hinc.global.rabbitmq.output.broadcast}")
    private String outputBroadcast;

    @Value("${hinc.global.rabbitmq.output.groupcast}")
    private String outputGroupcast;

    @Value("${hinc.global.rabbitmq.output.unicast}")
    private String outputUnicast;

    @Autowired
    public ResourceService(RabbitTemplate rabbitTemplate, ResourceRepository resourceRepository, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.resourceRepository = resourceRepository;
        this.objectMapper = objectMapper;
    }

    public List<Resource> queryResources(int timeout, String id, String group, int limit, boolean rescan) throws JsonProcessingException {
        String exchange = getDestinationExchange(id,group);
        String routing_key = getDestinationRoutingKey(id,group);

        //TODO Broadcast QUERY_IOT_UNIT (SenderID:UUID of Global, ResponseTopic: Temporary)
        HincMessage queryMessage = new HincMessage();
        queryMessage.setMsgType(HINCMessageType.QUERY_IOT_UNIT);
        //TODO change ID
        queryMessage.setSenderID("myID");
        queryMessage.setReceiverID(id);
        queryMessage.setDestination(exchange,routing_key);
        queryMessage.setReply(globalInputExchange, "");

        queryMessage.setUuid(UUID.randomUUID().toString());

        String payload = "";
        if (rescan) {
            payload = "rescan";
        }
        queryMessage.setPayload(payload);

        byte[] byteMessage = objectMapper.writeValueAsBytes(queryMessage);
        rabbitTemplate.convertAndSend(exchange, routing_key, byteMessage);

        if(timeout>0){
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //TODO temporary queue for timeout and limit
        List<Resource> result;
        if(limit>0) {
            Pageable resultLimit = PageRequest.of(0, limit);
            Page<Resource> resourcePage = resourceRepository.findAll(resultLimit);
            result = resourcePage.getContent();
        }else{
            result = resourceRepository.findAll();
        }

        return result;
    }



    private String getDestinationExchange(String id, String group){
        if(id!=null){
            return this.outputUnicast;
        }

        if(group != null){
            return this.outputGroupcast;
        }

        return this.outputBroadcast;
    }

    private String getDestinationRoutingKey(String id, String group){
        if(id!=null){
            return id;
        }

        if(group != null){
            return group;
        }

        //broadcast --> routing key is ignored
        return "";
    }
}
