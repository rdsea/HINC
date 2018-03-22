package sinc.hinc.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.processing.HincMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class GlobalCommunicationManager {
    private ObjectMapper objectMapper = new ObjectMapper();

    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Channel managementChannel = null;
    private Channel publishChannel = null;

    private String outgoingExchange = "global_outgoing_topic";
    private String incomingExchange = "global_incoming_direct";

    private Map<HINCMessageType, UnmarshallingConsumer> messageTypeQueues = new HashMap<>();

    public GlobalCommunicationManager(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
        connect(connectionFactory);
    }

    public void connect(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
        factory = connectionFactory;
        connection = factory.newConnection();
        managementChannel = connection.createChannel();
        publishChannel = connection.createChannel();

        setUpExchanges();
    }

    public void disconnect() throws IOException, TimeoutException {
        managementChannel.close();
        publishChannel.close();
        connection.close();
    }


    public void setUpExchanges() throws IOException {
        //declare/create outgoing exchange
        /* TODO tweak exchange settings
        Map<String, Object> exchangeArguments = new HashMap<>();
        managementChannel.exchangeDeclare(outgoingExchange, BuiltinExchangeType.TOPIC, true, false, true, exchangeArguments);
        */
        managementChannel.exchangeDeclare(outgoingExchange, BuiltinExchangeType.TOPIC, true);

        //declare/create incoming exchange
        /* TODO tweak exchange settings
        exchangeArguments = new HashMap<>();
        managementChannel.exchangeDeclare(incomingExchange, BuiltinExchangeType.DIRECT, true, false, false, exchangeArguments);
        */
        managementChannel.exchangeDeclare(incomingExchange, BuiltinExchangeType.DIRECT, true);
    }


    public void addMessageHandler(IMessageHandler messageHandler) throws IOException {
        String queueName = messageHandler.getMessageType().name();

        //TODO create Consumer with messageHandler
        Map<String, Object> queueArguments = new HashMap<>();
        managementChannel.queueDeclare(queueName, true, true, true, queueArguments);
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

    //TODO maybe add convenient methods --> broadcast, entire group, single receiver
    public void publishMessage(String routing_key, HincMessage hincMessage) throws IOException {

        AMQP.BasicProperties basicProperties = null;

        byte[] message = objectMapper.writeValueAsBytes(hincMessage);
        //TODO check basicproperties and other flags (boolean mandatory, boolean immediate)
        publishChannel.basicPublish(outgoingExchange, routing_key, basicProperties, message);

    }

    public void addLocalManagementService(String id){

    }

    public void removeLocalManagementService(String id){

    }


    //TODO manage results -> callback queues


}
