package sinc.hinc.local.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.communication.MessageDistributingConsumer;
import sinc.hinc.common.utils.HincConfiguration;
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

    private MessageDistributingConsumer globalMessageCosumer;

    private String groupName;
    private String id;
    private String globalExchange;

    private static LocalCommunicationManager localCommunicationManager;

    protected LocalCommunicationManager(String host, String group, String id, String globalExchange) throws IOException, TimeoutException {
        groupName = group;
        this.id = id;
        this.globalExchange = globalExchange;
        initConnection(host);
        connect();
    }

    public static void initialize(String host, String group, String id, String globalExchange){
        try {
            localCommunicationManager = new LocalCommunicationManager(host, group, id, globalExchange);
        } catch (Exception e) {
            // TODO log
            e.printStackTrace();
        }
    }

    public static LocalCommunicationManager getInstance(){
        return localCommunicationManager;
    }

    private void initConnection(String host) throws IOException, TimeoutException{
        factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        publishChannel = connection.createChannel();
        publishChannel.exchangeDeclare(this.getExchange(), "fanout");
    }

    private void connect() throws IOException, TimeoutException {
        String queueName = groupName + "." + id;
        setUpQueue(queueName);

        globalMessageCosumer = new MessageDistributingConsumer(connection.createChannel(), queueName);
        registerMessageHandler();
        globalMessageCosumer.start();

        registerAtGlobal();
    }

    private void setUpQueue(String queueName) throws IOException {
        Map<String,Object> queueArguments = new HashMap<>();
        //TODO escape dots in groupName and id

        publishChannel.queueDeclare(queueName, true, false, true, queueArguments);
        publishChannel.queueBind(queueName, this.getExchange(), "");
    }

    public void disconnect() throws IOException, TimeoutException {
        publishChannel.close();
        connection.close();
    }

    public void registerAtGlobal(){
        HincMessage registerMessage = new HincMessage();
        registerMessage.setSenderID(id);
        registerMessage.setDestination(globalExchange, "");
        registerMessage.setReply(id, groupName);
        registerMessage.setPayload(HincConfiguration.getLocalMeta().toJson());

        registerMessage.setMsgType(HINCMessageType.REGISTER_LMS);
        this.sendMessage(registerMessage);
    }

    public void addMessageHandler(HINCMessageHandler messageHandler){
        globalMessageCosumer.addMessageHandler(messageHandler);
    }

    public void sendMessage(HincMessage hincMessage){
        try {

            AMQP.BasicProperties basicProperties = null;
            byte[] message = objectMapper.writeValueAsBytes(hincMessage);

            // TODO check basicproperties and other flags (boolean mandatory, boolean immediate)
            publishChannel.basicPublish(
                    hincMessage.getDestination().getExchange(),
                    hincMessage.getDestination().getRoutingKey(),
                    basicProperties, message);

        } catch (IOException e) {
            //TODO errorhandling
            e.printStackTrace();
        }
    }


    private void registerMessageHandler(){
        this.addMessageHandler(new HandleFetchResources());
        this.addMessageHandler(new HandleFetchProviders());
        this.addMessageHandler(new HandleControl());
    }

    public String getExchange(){
        return this.groupName;
    }


}
