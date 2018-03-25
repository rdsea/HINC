package sinc.hinc.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.messagehandlers.HandleControlResult;
import sinc.hinc.communication.messagehandlers.HandleSynReply;
import sinc.hinc.communication.messagehandlers.HandleUpdateInformationSingleIotUnit;
import sinc.hinc.communication.processing.HincMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class GlobalCommunicationManager {
    private ObjectMapper objectMapper = new ObjectMapper();
    private static GlobalCommunicationManager globalCommunicationManager;

    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Channel managementChannel = null;
    private Channel publishChannel = null;

    private String outgoingBroadcast = "global_outgoing_broadcast";
    private String outgoingGroupcast = "global_outgoing_groupcast";
    private String outgoingUnicast = "global_outgoing_unicast";
    private String incomingExchange = "global_incoming_direct";

    private Map<HINCMessageType, UnmarshallingConsumer> messageTypeQueues = new HashMap<>();

    //TODO make singleton
    private GlobalCommunicationManager(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
        connect(connectionFactory);
    }

    public static GlobalCommunicationManager getInstance(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
        if(globalCommunicationManager == null){
            globalCommunicationManager = new GlobalCommunicationManager(connectionFactory);
        }
        return globalCommunicationManager;
    }

    public static GlobalCommunicationManager getInstance() throws IOException, TimeoutException {
        if(globalCommunicationManager == null){
            globalCommunicationManager = new GlobalCommunicationManager(getDefaultConnectionFactory());
        }
        return globalCommunicationManager;
    }

    //TODO change connectionfactory behaviour
    private static ConnectionFactory getDefaultConnectionFactory(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        return connectionFactory;
    }

    public void connect(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
        factory = connectionFactory;
        connection = factory.newConnection();
        managementChannel = connection.createChannel();
        publishChannel = connection.createChannel();

        setUpExchanges();
        registerMessageHandler();
    }

    public void disconnect() throws IOException, TimeoutException {
        managementChannel.close();
        publishChannel.close();
        connection.close();
    }


    public void setUpExchanges() throws IOException {
        //declare/create outgoing broadcast exchange
        /* TODO tweak exchange settings
        Map<String, Object> exchangeArguments = new HashMap<>();
        managementChannel.exchangeDeclare(outgoingExchange, BuiltinExchangeType.TOPIC, true, false, true, exchangeArguments);
        */
        managementChannel.exchangeDeclare(outgoingBroadcast, BuiltinExchangeType.FANOUT, true);

        //declare/create outgoing groupcast exchange
        /* TODO tweak exchange settings
        Map<String, Object> exchangeArguments = new HashMap<>();
        managementChannel.exchangeDeclare(outgoingExchange, BuiltinExchangeType.TOPIC, true, false, true, exchangeArguments);
        */
        managementChannel.exchangeDeclare(outgoingGroupcast, BuiltinExchangeType.DIRECT, true);

        //declare/create outgoing unicast exchange
        /* TODO tweak exchange settings
        Map<String, Object> exchangeArguments = new HashMap<>();
        managementChannel.exchangeDeclare(outgoingExchange, BuiltinExchangeType.TOPIC, true, false, true, exchangeArguments);
        */
        managementChannel.exchangeDeclare(outgoingUnicast, BuiltinExchangeType.DIRECT, true);


        //declare/create incoming exchange
        /* TODO tweak exchange settings
        exchangeArguments = new HashMap<>();
        managementChannel.exchangeDeclare(incomingExchange, BuiltinExchangeType.DIRECT, true, false, false, exchangeArguments);
        */
        managementChannel.exchangeDeclare(incomingExchange, BuiltinExchangeType.DIRECT, true);
    }


    public void addMessageHandler(IMessageHandler messageHandler) throws IOException {
        String queueName = messageHandler.getMessageType().name();

        //TODO tweak queue settings
        Map<String, Object> queueArguments = new HashMap<>();
        managementChannel.queueDeclare(queueName, true, true, false, queueArguments);
        //bindqueue

        String routing_key = messageHandler.getMessageType().name();
        managementChannel.queueBind(queueName, incomingExchange, routing_key);

        UnmarshallingConsumer consumer = new UnmarshallingConsumer(connection.createChannel(), messageHandler, queueName);
        removeMessageHandler(messageHandler);
        messageTypeQueues.put(messageHandler.getMessageType(), consumer);
        consumer.start();
    }

    public void removeMessageHandler(IMessageHandler messageHandler) throws IOException {
        UnmarshallingConsumer consumer = messageTypeQueues.remove(messageHandler.getMessageType());
        if(consumer!=null){
            consumer.close();

            String routing_key = messageHandler.getMessageType().name();
            managementChannel.queueUnbind(messageHandler.getMessageType().name(), incomingExchange, routing_key);
            managementChannel.queueDelete(messageHandler.getMessageType().name());
        }
    }

    public void broadcastMessage(HincMessage hincMessage) throws IOException {
        AMQP.BasicProperties basicProperties = null;
        byte[] message = objectMapper.writeValueAsBytes(hincMessage);
        //TODO check basicproperties and other flags (boolean mandatory, boolean immediate)
        publishChannel.basicPublish(outgoingBroadcast, "", basicProperties, message);
    }

    public void groupcastMessage(String group, HincMessage hincMessage) throws IOException {
        AMQP.BasicProperties basicProperties = null;
        byte[] message = objectMapper.writeValueAsBytes(hincMessage);
        //TODO check basicproperties and other flags (boolean mandatory, boolean immediate)
        publishChannel.basicPublish(outgoingGroupcast, group, basicProperties, message);
    }

    public void unicastMessage(String group, String lmsId, HincMessage hincMessage) throws IOException {
        String routingKey = group + "." + lmsId;

        AMQP.BasicProperties basicProperties = null;
        byte[] message = objectMapper.writeValueAsBytes(hincMessage);
        //TODO check basicproperties and other flags (boolean mandatory, boolean immediate)
        publishChannel.basicPublish(outgoingUnicast, routingKey, basicProperties, message);
    }

    public void addLocalManagementService(String lmsQueue, String lmsGroup, String lmsID) throws IOException {
        managementChannel.queueBind(lmsQueue, outgoingBroadcast, "");
        managementChannel.queueBind(lmsQueue, outgoingGroupcast, lmsGroup);
        managementChannel.queueBind(lmsQueue, outgoingUnicast, lmsGroup+"." + lmsID);
    }

    public void removeLocalManagementService(String lmsQueue, String lmsGroup, String lmsID) throws IOException {
        managementChannel.queueUnbind(lmsQueue, outgoingBroadcast, "");
        managementChannel.queueUnbind(lmsQueue, outgoingGroupcast, lmsGroup);
        managementChannel.queueUnbind(lmsQueue, outgoingUnicast, lmsGroup+"." + lmsID);
    }


    private void registerMessageHandler() throws IOException {
        this.addMessageHandler(new HandleControlResult());
        this.addMessageHandler(new HandleSynReply(this));
        this.addMessageHandler(new HandleUpdateInformationSingleIotUnit());
    }

    //TODO manage results -> callback queues


    //TODO remove main - it's just for testing purposes
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        GlobalCommunicationManager globalCommunicationManager = new GlobalCommunicationManager(connectionFactory);

        int i = 0;
        while(true){
            HincMessage message = new HincMessage();
            message.setHincMessageType(HINCMessageType.CONTROL);
            System.out.println("publish from global");
            i++;
            i = i%3;
            switch (i){
                case 0:
                    message.setPayload("broadcast");
                    globalCommunicationManager.broadcastMessage(message);
                    break;
                case 1:
                    message.setPayload("groupcast");
                    globalCommunicationManager.groupcastMessage("group", message);
                    break;
                case 2:
                    message.setPayload("unicast");
                    globalCommunicationManager.unicastMessage("group", "id", message);
                    break;
            }


            try {
                Thread.sleep(5000);
            }catch (InterruptedException e){
                System.out.println("interrupted");
            }
        }

    }

}
