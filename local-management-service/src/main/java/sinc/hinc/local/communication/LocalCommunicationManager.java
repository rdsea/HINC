package sinc.hinc.local.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.communication.MessageDistributingConsumer;
import sinc.hinc.common.utils.HincConfiguration;

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
        //TODO change to correct messagetype
        registerMessage.setMsgType(HINCMessageType.SYN_REPLY.toString());
        //registerMessage.setSenderID(HincConfiguration.getMyUUID());
        registerMessage.setSenderID(id);
        //registerMessage.setTopic(HincConfiguration.getGroupName());
        registerMessage.setTopic(groupName);
        registerMessage.setRoutingKey("");
        registerMessage.setFeedbackTopic("");
        registerMessage.setPayload(HincConfiguration.getLocalMeta().toJson());

        registerMessage.setGroup(groupName);
        registerMessage.setHincMessageType(HINCMessageType.SYN_REPLY);

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
            publishChannel.basicPublish(hincMessage.getTopic(), hincMessage.getRoutingKey(), basicProperties, message);

        } catch (IOException e) {
            //TODO errorhandling
            e.printStackTrace();
        }
    }


    private void registerMessageHandler(){
        // TODO refactor message handlers to HINCMessageHandler
        /*
            this.addMessageHandler(new HandleControl(this));
            this.addMessageHandler(new HandleSynRequest(this));
            this.addMessageHandler(new HandleUpdateInfoBase(this));
        */
    }

    public String getExchange(){
        return this.groupName;
    }


    //TODO remove - for testing purpose only
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        String group = "group";
        String id = "id";

        LocalCommunicationManager.initialize("localhost", group, id, "global_incoming_direct");
        LocalCommunicationManager localCommunicationManager = LocalCommunicationManager.getInstance();

        HincMessage register = new HincMessage();
        register.setHincMessageType(HINCMessageType.SYN_REPLY);
        register.setGroup("group");
        register.setSenderID("id");
        localCommunicationManager.sendMessage(register);

        int i = 0;
        while(true){
            i++;
            HincMessage message = testMessage(i);
            System.out.println("publish to global");
            localCommunicationManager.sendMessage(message);
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
