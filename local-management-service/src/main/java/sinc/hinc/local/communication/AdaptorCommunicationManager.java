package sinc.hinc.local.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.communication.MessageDistributingConsumer;
import sinc.hinc.common.model.payloads.Control;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.communication.messagehandlers.*;
import sinc.hinc.local.plugin.Adaptor;
import sinc.hinc.local.plugin.AdaptorManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class AdaptorCommunicationManager {
    private ObjectMapper objectMapper = new ObjectMapper();

    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Channel publishChannel = null;

    private MessageDistributingConsumer messageDistributingCosumer;

    private String groupName;
    private String id;

    private static AdaptorCommunicationManager adaptorCommunicationManager;

    protected AdaptorCommunicationManager(String uri, String group, String id) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        groupName = group;
        this.id = id;
        initConnection(uri);
        connect();
    }

    public static void initialize(String host, String group, String id){
        try {
            adaptorCommunicationManager = new AdaptorCommunicationManager(host, group, id);
        } catch (Exception e) {
            // TODO log
            e.printStackTrace();
        }
    }

    public static AdaptorCommunicationManager getInstance(){
        return adaptorCommunicationManager;
    }

    public void disconnect() throws IOException, TimeoutException {
        publishChannel.close();
        connection.close();
    }

    public String getExchange(){
        return this.groupName+".adaptors";
    }

    public String getRoutingKey() { return this.groupName+".local";}


    public void addMessageHandler(HINCMessageHandler messageHandler){
        messageDistributingCosumer.addMessageHandler(messageHandler);
    }

    public void sendMessage(HincMessage hincMessage){
        try {

            AMQP.BasicProperties basicProperties = null;
            byte[] message = objectMapper.writeValueAsBytes(hincMessage);

            if(hincMessage.getDestination().getRoutingKey()==null){
                hincMessage.getDestination().setRoutingKey("");
            }
            publishChannel.basicPublish(
                    hincMessage.getDestination().getExchange(),
                    hincMessage.getDestination().getRoutingKey(),
                    basicProperties, message);

        } catch (IOException e) {
            //TODO errorhandling
            e.printStackTrace();
        }
    }

    private void initConnection(String uri) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        factory = new ConnectionFactory();
        factory.setUri(uri);
        connection = factory.newConnection();
        publishChannel = connection.createChannel();
        publishChannel.exchangeDeclare(this.getExchange(), "direct");
    }

    private void connect() throws IOException, TimeoutException {
        String queueName = groupName + ".local." + id;
        setUpQueue(queueName);

        messageDistributingCosumer = new MessageDistributingConsumer(connection.createChannel(), queueName);
        registerMessageHandler();
        messageDistributingCosumer.start();
    }

    private void setUpQueue(String queueName) throws IOException {
        Map<String,Object> queueArguments = new HashMap<>();

        publishChannel.queueDeclare(queueName, true, false, true, queueArguments);
        publishChannel.queueBind(queueName, this.getExchange(), this.getRoutingKey());
    }


    private void registerMessageHandler(){
        this.addMessageHandler(new HandleResourcesUpdate());
        this.addMessageHandler(new HandleProviderUpdate());
        this.addMessageHandler(new HandleControlResult());
        this.addMessageHandler(new HandleFetchResources());
        this.addMessageHandler(new HandleFetchProviders());
        this.addMessageHandler(new HandleControl());
        this.addMessageHandler(new HandleRegisterAdaptor());
        this.addMessageHandler(new HandleDeregisterAdaptor());
        this.addMessageHandler(new HandleProvision());
    }


}
