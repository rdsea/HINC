package sinc.hinc.local.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.local.communication.LocalCommunicationManager;

public class HandleSynRequest extends HINCMessageHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalCommunicationManager localCommunicationManager;

    public HandleSynRequest(LocalCommunicationManager localCommunicationManager) {
        super(HINCMessageType.SYN_REQUEST);
        this.localCommunicationManager = localCommunicationManager;
    }

    @Override
    protected void doHandle(HincMessage msg) {
        logger.debug("received " + msg.toString());
        //TODO implement MessageHandler


        //TODO check if adapted code works / refactor messy code
        HincLocalMeta meta = HincConfiguration.getLocalMeta();
        /*System.out.println("GETTING HANDLLER. NUMBER of HANDERS: " + Main.getListener().getHandlers().size());
        for (HINCMessageListener.Handler handler: Main.getListener().getHandlers()){
            System.out.println("adding handler ...: " + handler.getTopic() + " -- " + handler.getMessageType());
            meta.hasHandler(handler.getTopic(), handler.getMessageType(), handler.getHandlerMethod().getClass().getName());
        }*/
        //TODO add metadata to message: handler.getTopic(), handler.getMessageType(), handler.getClass

        HincMessage reply = new HincMessage(HINCMessageType.SYN_REPLY.toString(), HincConfiguration.getMyUUID(), msg.getFeedbackTopic(), "", meta.toJson());

        localCommunicationManager.sendToGlobal(reply);
    }
}
