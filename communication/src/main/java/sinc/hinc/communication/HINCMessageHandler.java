package sinc.hinc.communication;


import sinc.hinc.common.metadata.HINCMessageType;

public abstract class HINCMessageHandler {

    protected HINCMessageType messageType;
    protected HINCMessageHandler nextHandler;

    public void setNextHandler(HINCMessageHandler handler){
        this.nextHandler = handler;
    }

    public HINCMessageHandler getNextHandler(){
        return this.nextHandler;
    }

    public HincMessage handleMessage(HincMessage msg){
        if(this.messageType == msg.getHincMessageType()){
            return doHandle(msg);
        }
        else if(this.nextHandler != null){
            return nextHandler.handleMessage(msg);
        }else{
            return null;
        }
    }

    abstract protected HincMessage doHandle(HincMessage msg);
}
