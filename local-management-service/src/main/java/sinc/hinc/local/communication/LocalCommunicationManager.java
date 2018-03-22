package sinc.hinc.local.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.MessageDistributingConsumer;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HINCMessageListener;
import sinc.hinc.communication.processing.HincMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class LocalCommunicationManager {
    private ObjectMapper objectMapper = new ObjectMapper();

    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Channel publishChannel = null;

    private MessageDistributingConsumer messageDistributingConsumer;

    private String groupName;
    private String id;
    private String globalExchange;

    public LocalCommunicationManager(ConnectionFactory connectionFactory, String group, String id, String globalExchange) throws IOException, TimeoutException {
        groupName = group;
        this.id = id;
        this.globalExchange = globalExchange;
        connect(connectionFactory);
    }

    public void connect(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
        factory = connectionFactory;
        connection = factory.newConnection();
        publishChannel = connection.createChannel();

        String queueName = groupName + "." + id;
        setUpQueue(queueName);

        messageDistributingConsumer = new MessageDistributingConsumer(connection.createChannel(), queueName);
        messageDistributingConsumer.start();

        registerAtGlobal();
    }

    private void setUpQueue(String queueName) throws IOException {
        Map<String,Object> queueArguments = new HashMap<>();
        //TODO escape dots in groupName and id

        publishChannel.queueDeclare(queueName, true, true, true, queueArguments);
    }

    public void disconnect() throws IOException, TimeoutException {
        publishChannel.close();
        connection.close();
    }

    public void registerAtGlobal(){
        HincMessage hincMessage = new HincMessage();
        //TODO change to correct messagetype
        hincMessage.setMsgType(HINCMessageType.SYN_REPLY.toString());

        //TODO send message to GMS --> GMS will then bind LMS Queue to GMS Exchange
    }

    public void addMessageHandler(IMessageHandler messageHandler){
        messageDistributingConsumer.addMessageHandler(messageHandler);
    }

    public void publishMessage(HincMessage hincMessage) throws IOException {
        AMQP.BasicProperties basicProperties = null;

        byte[] message = objectMapper.writeValueAsBytes(hincMessage);
        //TODO check basicproperties and other flags (boolean mandatory, boolean immediate)
        String routing_key = hincMessage.getHincMessageType().name();
        publishChannel.basicPublish(globalExchange, routing_key, basicProperties, message);
    }


}
