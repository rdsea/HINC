package sinc.hinc.common.communication;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void handleMessage(byte[] message) throws IOException {
        HincMessage hincMessage = objectMapper.readValue(message, HincMessage.class);
        logger.debug("received and converted byte message to: " + hincMessage.toString());
        handleMessage(hincMessage);
    }

    public void handleMessage(HincMessage msg){
        logger.debug(this.messageType.name());
        if(this.messageType == msg.getMsgType()){
            doHandle(msg);
            return;
        }
        else if(this.nextHandler != null){
            nextHandler.handleMessage(msg);
            return;
        }
        logger.info("no handler found for message " + msg.getMsgType());
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

    abstract protected void doHandle(HincMessage msg);
}
