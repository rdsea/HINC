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
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.communication.messagehandlers.HandleProviderUpdate;
import sinc.hinc.local.communication.messagehandlers.HandleResourcesUpdate;
import sinc.hinc.local.plugin.Adaptor;
import sinc.hinc.local.plugin.AdaptorManager;

import java.io.IOException;
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

    protected AdaptorCommunicationManager(String host, String group, String id) throws IOException, TimeoutException {
        groupName = group;
        this.id = id;
        initConnection(host);
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

            publishChannel.basicPublish(
                    hincMessage.getDestination().getExchange(),
                    hincMessage.getDestination().getRoutingKey(),
                    basicProperties, message);

        } catch (IOException e) {
            //TODO errorhandling
            e.printStackTrace();
        }
    }

    private void initConnection(String host) throws IOException, TimeoutException{
        factory = new ConnectionFactory();
        factory.setHost(host);
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
        this.addMessageHandler(new HandleResourcesUpdate(HINCMessageType.UPDATE_RESOURCES));
        this.addMessageHandler(new HandleProviderUpdate(HINCMessageType.UPDATE_PROVIDER));
    }

    public static void main(String[] args){
        String group = "test";
        String host = "localhost";
        String id = "xx";

        AdaptorCommunicationManager.initialize(host, group, id);

        AdaptorManager adaptorManager = AdaptorManager.getInstance();

        Adaptor adaptor = new Adaptor();
        HashMap<String, String> settings = new HashMap<>();
        settings.put("routingKey", "testy");
        adaptor.setSettings(settings);
        adaptor.setName("test adaptor");

        adaptorManager.addAdaptor(adaptor.getName(), adaptor);
        adaptorManager.scanAll();
    }

}
