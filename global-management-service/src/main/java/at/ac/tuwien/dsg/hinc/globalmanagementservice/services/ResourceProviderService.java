package at.ac.tuwien.dsg.hinc.globalmanagementservice.services;

import at.ac.tuwien.dsg.hinc.globalmanagementservice.repository.LocalMSRepository;
import at.ac.tuwien.dsg.hinc.globalmanagementservice.repository.ProviderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.ResourceProvider;
import sinc.hinc.common.model.payloads.Control;
import sinc.hinc.common.model.payloads.ControlResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceProviderService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;

    private final ProviderRepository providerRepository;
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
    public ResourceProviderService(RabbitTemplate rabbitTemplate, ProviderRepository providerRepository, ObjectMapper objectMapper, LocalMSRepository localMSRepository, RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.providerRepository = providerRepository;
        this.objectMapper = objectMapper;
        this.localMSRepository = localMSRepository;
        this.rabbitAdmin = rabbitAdmin;
    }

    public List<ResourceProvider> queryResourceProviders(int timeout, int limit, String query) throws JsonProcessingException {
        String id = null;
        String group = null;

        String exchange = getDestinationExchange(id,group);
        String routing_key = getDestinationRoutingKey(id,group);


        HincMessage queryMessage = new HincMessage();
        queryMessage.setMsgType(HINCMessageType.FETCH_PROVIDERS);
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
            return getProviders(id, group, limit);
        }else{
            return queryProviders(id, group, limit, query);
        }

    }


    public ControlResult sendControl(Control control) throws IOException {

        return null;
    }

    public ControlResult sendControl(String providerId, String controlPointId, JsonNode parameters) throws IOException {
        Control control = new Control();
        control.setResourceProviderUuid(providerId);
        control.setControlPointUuid(controlPointId);
        control.setParameters(parameters);

        return sendControl(control);
    }


    private List<ResourceProvider> getProviders(String id, String group, int limit){
        // TODO groupcast + unicast
        List<ResourceProvider> result = new ArrayList<>();


        if(limit>0) {
            result = providerRepository.readAll(limit);
        }else{
            result = providerRepository.readAll();
        }

        return result;
    }

    private List<ResourceProvider> queryProviders(String id, String group, int limit, String query){
        List<ResourceProvider> result = new ArrayList<>();

        if(limit>0) {
            result = providerRepository.query(query, limit);
        }else{
            result = providerRepository.query(query);
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
