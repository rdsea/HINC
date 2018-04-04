package sinc.hinc.common.communication;

public abstract class HINCMessageHandler {

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
        if(this.messageType == msg.getHincMessageType()){
            doHandle(msg);
        }
        else if(this.nextHandler != null){
            nextHandler.handleMessage(msg);
        }
    }

    abstract protected void doHandle(HincMessage msg);
}
