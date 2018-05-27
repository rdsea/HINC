/*
package sinc.hinc.common.communication;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageDistributingConsumer extends DefaultConsumer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ObjectMapper objectMapper = new ObjectMapper();
    private HINCMessageHandler handler;
    private String queue;

    public MessageDistributingConsumer(Channel channel, String queue) {
        super(channel);
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.queue = queue;
    }


    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body)
            throws IOException {

        HincMessage hincMessage = objectMapper.readValue(body, HincMessage.class);
        logger.debug("received and distributing: " + hincMessage.toString());
        this.handler.handleMessage(hincMessage);
    }


    public void addMessageHandler(HINCMessageHandler handler){
        if(this.handler == null){
            this.handler = handler;
        }else{
            HINCMessageHandler cur = this.handler;
            while(cur.getNextHandler() != null){
                cur = cur.getNextHandler();
            }
            cur.setNextHandler(handler);
        }
    }

    public void start() throws IOException {
        getChannel().basicConsume(queue, , this);
    }

}
*/
