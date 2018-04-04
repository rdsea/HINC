package sinc.hinc.communication.messagehandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import sinc.hinc.communication.HINCMessageHandler;
import sinc.hinc.communication.HincMessage;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class UnmarshallingConsumer extends DefaultConsumer {
    private HINCMessageHandler messageHandler;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String queue;

    public UnmarshallingConsumer(Channel channel, HINCMessageHandler messageHandler, String queue) {
        super(channel);
        this.messageHandler = messageHandler;
        this.queue = queue;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body)
            throws IOException {

        HincMessage hincMessage = objectMapper.readValue(body, HincMessage.class);
        messageHandler.handleMessage(hincMessage);

    }

    public void close(){
        //TODO errorhandling
        try {
            this.getChannel().close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        getChannel().basicConsume(queue, true, this);
    }
}