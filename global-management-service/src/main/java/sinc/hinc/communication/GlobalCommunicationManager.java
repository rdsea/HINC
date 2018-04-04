package sinc.hinc.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.messagehandlers.HandleControlResult;
import sinc.hinc.communication.messagehandlers.HandleSynReply;
import sinc.hinc.communication.messagehandlers.HandleUpdateInformationSingleIotUnit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class GlobalCommunicationManager {
    private ObjectMapper objectMapper = new ObjectMapper();
    private static GlobalCommunicationManager globalCommunicationManager;

    private MessageDistributingConsumer messageDistributingConsumer;

    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Channel managementChannel = null;
    private Channel publishChannel = null;

    private String host;
    private String outgoingBroadcast = "global_outgoing_broadcast";
    private String outgoingGroupcast = "global_outgoing_groupcast";
    private String outgoingUnicast = "global_outgoing_unicast";
    private String incomingExchange = "global_incoming_direct";

    private GlobalCommunicationManager(String host) throws IOException, TimeoutException {
        connect(host);
    }

    public static GlobalCommunicationManager getInstance(){
        return globalCommunicationManager;
    }

    public static void initialize(String host){
        try {
            globalCommunicationManager = new GlobalCommunicationManager(host);
        } catch (Exception e) {
            // TODO log
            e.printStackTrace();
        }
    }


    private void connect(String host) throws IOException, TimeoutException {
        this.host = host;

        factory = new ConnectionFactory();
        factory.setHost(this.host);
        connection = factory.newConnection();
        managementChannel = connection.createChannel();
        publishChannel = connection.createChannel();

        setUpInput();
        setUpExchanges();

        messageDistributingConsumer = new MessageDistributingConsumer(connection.createChannel(), incomingExchange);
        registerMessageHandler();
        messageDistributingConsumer.start();
    }

    public void disconnect() throws IOException, TimeoutException {
        managementChannel.close();
        publishChannel.close();
        connection.close();
    }

    private void setUpInput() throws IOException {
        Map<String,Object> queueArguments = new HashMap<>();

        publishChannel.queueDeclare(incomingExchange, true, false, true, queueArguments);
        //declare/create incoming exchange
        /* TODO tweak exchange settings
        exchangeArguments = new HashMap<>();
        managementChannel.exchangeDeclare(incomingExchange, BuiltinExchangeType.FANOUT, true, false, false, exchangeArguments);
        */
        managementChannel.exchangeDeclare(incomingExchange, BuiltinExchangeType.FANOUT, true);
        managementChannel.queueBind(incomingExchange, incomingExchange, "");
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
    }


    public void addMessageHandler(HINCMessageHandler messageHandler) throws IOException {
        messageDistributingConsumer.addMessageHandler(messageHandler);
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


    //TODO remove main - it's just for testing purposes
    public static void main(String[] args) throws Exception {

        GlobalCommunicationManager.initialize("localhost");
        GlobalCommunicationManager globalCommunicationManager = GlobalCommunicationManager.getInstance();

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
