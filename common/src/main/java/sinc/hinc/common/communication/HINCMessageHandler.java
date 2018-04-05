package sinc.hinc.common.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HINCMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected HINCMessageType messageType;
    protected HINCMessageHandler nextHandler;

    public HINCMessageHandler(HINCMessageType messageType){
        this.messageType = messageType;
    }

    public void setNextHandler(HINCMessageHandler handler){
        this.nextHandler = handler;
    }

    public HINCMessageHandler getNextHandler(){
        return this.nextHandler;
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

    abstract protected void doHandle(HincMessage msg);
}
