package sinc.hinc.common.communication;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import java.io.IOException;

public abstract class HINCMessageHandler {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected ObjectMapper objectMapper;

    protected HINCMessageType messageType;
    protected HINCMessageHandler nextHandler;

    public HINCMessageHandler(HINCMessageType messageType){
        this.messageType = messageType;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void setNextHandler(HINCMessageHandler handler){
        this.nextHandler = handler;
    }

    public HINCMessageHandler getNextHandler(){
        return this.nextHandler;
    }

    public byte[] handleMessage(byte[] bytes) throws IOException {
        HincMessage hincMessage = objectMapper.readValue(bytes, HincMessage.class);
        logger.debug("received and converted byte message to: " + hincMessage.toString());

        HincMessage reply = handleMessage(hincMessage);
        if(reply != null){
            return objectMapper.writeValueAsBytes(reply);
        }else{
            return null;
        }
    }


    public HincMessage handleMessage(HincMessage msg){
        logger.debug(this.messageType.name());
        if(this.messageType == msg.getMsgType()){
            return doHandle(msg);
        }
        else if(this.nextHandler != null){
            return nextHandler.handleMessage(msg);
        }
        logger.info("no handler found for message " + msg.getMsgType());
        return null;
    }

    public void addMessageHandler(HINCMessageHandler handler){
        if(this.nextHandler == null){
            this.nextHandler = handler;
        }else{
            HINCMessageHandler cur = nextHandler;
            while(cur.getNextHandler() != null){
                cur = cur.getNextHandler();
            }
            cur.setNextHandler(handler);
        }
    }

    abstract protected HincMessage doHandle(HincMessage msg);
}
