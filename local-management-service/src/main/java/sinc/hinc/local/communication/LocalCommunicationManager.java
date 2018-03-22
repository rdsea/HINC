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
import sinc.hinc.local.communication.messagehandlers.*;

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

    //TODO make singleton
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
        registerMessageHandler();
        messageDistributingConsumer.start();

        registerAtGlobal();
    }

    private void setUpQueue(String queueName) throws IOException {
        Map<String,Object> queueArguments = new HashMap<>();
        //TODO escape dots in groupName and id

        publishChannel.queueDeclare(queueName, true, false, true, queueArguments);
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


    private void registerMessageHandler(){
        this.addMessageHandler(new HandleControl());
        this.addMessageHandler(new HandleQueryIotProviders());
        this.addMessageHandler(new HandleQueryIotUnit());
        this.addMessageHandler(new HandleSynRequest());
        this.addMessageHandler(new HandleUpdateInfoBase());
    }


    //TODO remove - for testing purpose only
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        String group = "group";
        String id = "id";

        LocalCommunicationManager localCommunicationManager = new LocalCommunicationManager(connectionFactory, group, id, "global_incoming_direct");

        HincMessage register = new HincMessage();
        register.setHincMessageType(HINCMessageType.SYN_REPLY);
        register.setGroup("group");
        register.setSenderID("id");
        localCommunicationManager.publishMessage(register);

        int i = 0;
        while(true){
            i++;
            HincMessage message = testMessage(i);
            System.out.println("publish to global");
            localCommunicationManager.publishMessage(message);
            i = i%3;

            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                System.out.println("interrupted");
            }
        }

    }
    private static HincMessage testMessage(int i){
        HincMessage message = new HincMessage();
        message.setPayload("testpayload");

        switch (i%3) {
            case 0:message.setHincMessageType(HINCMessageType.CONTROL_RESULT);break;
            case 1:message.setHincMessageType(HINCMessageType.SYN_REPLY);break;
            case 2:message.setHincMessageType(HINCMessageType.UPDATE_INFORMATION_SINGLEIOTUNIT);break;
        }
        return message;
    }
}
