package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.services;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.model.LocalMS;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.LocalMSRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    public ResourceService(RabbitTemplate rabbitTemplate, ResourceRepository resourceRepository, ObjectMapper objectMapper, LocalMSRepository localMSRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.resourceRepository = resourceRepository;
        this.objectMapper = objectMapper;
        this.localMSRepository = localMSRepository;
    }

    public List<Resource> queryResources(int timeout, String id, String group, int limit, boolean rescan) throws JsonProcessingException {
        String exchange = getDestinationExchange(id,group);
        String routing_key = getDestinationRoutingKey(id,group);

        HincMessage queryMessage = new HincMessage();
        queryMessage.setMsgType(HINCMessageType.FETCH_RESOURCES);
        queryMessage.setUuid(globalId);

        queryMessage.setSenderID(globalId);
        queryMessage.setReply(globalInputExchange, "");
        queryMessage.setReceiverID(id);
        queryMessage.setDestination(exchange,routing_key);

        byte[] byteMessage = objectMapper.writeValueAsBytes(queryMessage);
        rabbitTemplate.convertAndSend(exchange, routing_key, byteMessage);

        if(timeout>0){
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //TODO temporary queue
        return getResources(id, group, limit);
    }

    private List<Resource> getResources(String id, String group, int limit){

        List<Resource> result = new ArrayList<>();

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
        }else{
            if(limit>0) {
                Pageable resultLimit = PageRequest.of(0, limit);
                Page<Resource> resourcePage = resourceRepository.findAll(resultLimit);
                result = resourcePage.getContent();
            }else{
                result = resourceRepository.findAll();
            }
        }

        if(limit>0) {
            result = result.subList(0, limit);
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
