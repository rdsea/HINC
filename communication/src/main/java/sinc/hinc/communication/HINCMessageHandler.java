package sinc.hinc.communication;


import sinc.hinc.common.metadata.HINCMessageType;

public abstract class HINCMessageHandler {

    protected HINCMessageHandler nextHandler;

    public void setNextHandler(HINCMessageHandler handler){
        this.nextHandler = handler;
    }

    public HINCMessageHandler getNextHandler(){
        return this.nextHandler;
    }

    public void handleMessage(HincMessage msg){
        if(this.acceptedMessageType() == msg.getHincMessageType()){
            doHandle(msg);
        }
        else if(this.nextHandler != null){
            nextHandler.handleMessage(msg);
        }
    }

    abstract protected HINCMessageType acceptedMessageType();
    abstract protected void doHandle(HincMessage msg);
}
