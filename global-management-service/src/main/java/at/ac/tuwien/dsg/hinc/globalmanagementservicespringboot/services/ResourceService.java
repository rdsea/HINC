package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.services;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.LocalMSRepository;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.ResourceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceService {

    private final RabbitTemplate rabbitTemplate;

    private final ResourceRepository resourceRepository;
    private final LocalMSRepository localMSRepository;

    private final ObjectMapper objectMapper;

    @Value("${hinc.global.rabbitmq.input}")
    private String globalInputExchange;

    @Value("${hinc.global.rabbitmq.output.broadcast}")
    private String outputBroadcast;

    @Value("${hinc.global.rabbitmq.output.groupcast}")
    private String outputGroupcast;

    @Value("${hinc.global.rabbitmq.output.unicast}")
    private String outputUnicast;

    @Value("${hinc.global.id}")
    private String globalId;

    @Value("${hinc.global.rabbitmq.input}")
    private String inputQueue;

    @Autowired
    public ResourceService(RabbitTemplate rabbitTemplate, ResourceRepository resourceRepository, ObjectMapper objectMapper, LocalMSRepository localMSRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.resourceRepository = resourceRepository;
        this.objectMapper = objectMapper;
        this.localMSRepository = localMSRepository;
    }

    public List<Resource> queryResources(int timeout, int limit, String query) throws JsonProcessingException {
        String group = null;
        String id = null;
        String exchange = getDestinationExchange(id,group);
        String routing_key = getDestinationRoutingKey(id,group);

        HincMessage queryMessage = new HincMessage();
        queryMessage.setMsgType(HINCMessageType.FETCH_RESOURCES);
        queryMessage.setUuid(globalId);

        queryMessage.setSenderID(globalId);
        queryMessage.setReceiverID(id);

        byte[] byteMessage = objectMapper.writeValueAsBytes(queryMessage);
        MessageProperties properties = new MessageProperties();
        properties.setReplyTo(inputQueue);
        Message msg = new Message(byteMessage, properties);
        rabbitTemplate.send(exchange, routing_key, msg);

        if(timeout>0){
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //TODO temporary queue
        if(StringUtils.isEmpty(query)){
            return getResources(id, group, limit);
        }else{
            return queryResources(id, group, limit, query);
        }
    }

    public Resource putMetadata(String resourceId, JsonNode metadata){
        Resource resource = resourceRepository.read(resourceId);
        if(resource != null) {
            //resource.setMetadata(metadata);
            resourceRepository.save(resource);
        }

        return resource;
    }

    private List<Resource> getResources(String id, String group, int limit){

        List<Resource> result = new ArrayList<>();

        /* TODO
        if(id != null && !id.isEmpty()){
            if (localMSRepository.findById(id).isPresent()) {
                LocalMS localMS = localMSRepository.findById(id).get();
                result = localMS.getResources();
            }
        }else if (group != null && !group.isEmpty()){
            List<LocalMS> localMSList = new ArrayList<>();
            localMSList = localMSRepository.findByGroup(group);
            for(LocalMS localMS:localMSList){
                result.addAll(localMS.getResources());
            }
        }else{*/
            if(limit>0) {

                result = resourceRepository.readAll(limit);
            }else{
                result = resourceRepository.readAll();
            }
        //}

        /*if(limit>0) {
            result = result.subList(0, limit);
        }*/

        return result;
    }

    private List<Resource> queryResources(String id, String group, int limit, String query){
        List<Resource> result = new ArrayList<>();

        if(limit>0) {
            result = resourceRepository.query(query, limit);
        }else{
            result = resourceRepository.query(query);
        }

        return result;
    }



    private String getDestinationExchange(String id, String group){
        if(id!=null && !id.isEmpty()){
            return this.outputUnicast;
        }

        if(group != null && !group.isEmpty()){
            return this.outputGroupcast;
        }

        return this.outputBroadcast;
    }

    private String getDestinationRoutingKey(String id, String group){
        if(id!=null && !id.isEmpty()){
            return id;
        }

        if(group != null && !group.isEmpty()){
            return group;
        }

        //broadcast --> routing key is ignored
        return "";
    }
}
