package sinc.hinc.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.processing.HincMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDistributingConsumer extends DefaultConsumer {
    private ObjectMapper objectMapper = new ObjectMapper();
    //TODO check concurrency
    private Map<HINCMessageType, List<IMessageHandler>> messageHandlerMap = new HashMap<>();
    private String queue;

    public MessageDistributingConsumer(Channel channel, String queue) {
        super(channel);
        this.queue = queue;
    }


    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body)
            throws IOException {

        HincMessage hincMessage = objectMapper.readValue(body, HincMessage.class);

        List<IMessageHandler> messageHandlerList = messageHandlerMap.get(hincMessage.getHincMessageType());
        if(messageHandlerList!=null) {
            messageHandlerList.parallelStream().forEach(x -> x.handleMessage(hincMessage));
        }
    }


    //TODO check concurrency
    public void addMessageHandler(IMessageHandler messageHandler){
        List<IMessageHandler> messageHandlerList = messageHandlerMap.get(messageHandler.getMessageType());

        if(messageHandlerList == null){
            messageHandlerList = new ArrayList<>();
            messageHandlerList.add(messageHandler);
            messageHandlerMap.put(messageHandler.getMessageType(), messageHandlerList);
        }else{
            messageHandlerList.add(messageHandler);
        }
    }

    public void start() throws IOException {
        getChannel().basicConsume(queue, true, this);
    }

}
