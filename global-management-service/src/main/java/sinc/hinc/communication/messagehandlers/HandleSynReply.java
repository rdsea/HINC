package sinc.hinc.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.GlobalCommunicationManager;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;

import java.io.IOException;

public class HandleSynReply implements IMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private GlobalCommunicationManager globalCommunicationManager;

    public HandleSynReply(GlobalCommunicationManager globalCommunicationManager){
        this.globalCommunicationManager = globalCommunicationManager;
    }

    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.SYN_REPLY;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        logger.debug("received " + hincMessage.toString());

        //TODO improve implementation --> hincMessage properties

        if(hincMessage.getSenderID()!=null && hincMessage.getGroup() != null){
            try {
                String queue = hincMessage.getGroup() + "." + hincMessage.getSenderID();
                globalCommunicationManager.addLocalManagementService(queue, hincMessage.getGroup(), hincMessage.getSenderID());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(hincMessage.getSenderID()!=null && hincMessage.getTopic() != null){
            try {
                String queue = hincMessage.getTopic() + "." + hincMessage.getSenderID();
                globalCommunicationManager.addLocalManagementService(queue, hincMessage.getTopic(), hincMessage.getSenderID());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /* TODO merge
        logger.debug("A message arrive, from: {}, type: {}, topic: {} ", msg.getSenderID(), msg.getMsgType(), msg.getTopic());
        if (msg.getMsgType().equals(HINCMessageType.SYN_REPLY.toString())) { // this will be always true !!
            logger.debug(" --> Yes, it is a SYN_REPLY message, adding the metadata");
            HincLocalMeta meta = HincLocalMeta.fromJson(msg.getPayload());
            logger.debug("  --> Meta: " + meta.toJson());
            // remove the exist meta if need before update
            Iterator<HincLocalMeta> metas = listOfHINCLocal.iterator();
            while(metas.hasNext()){
                HincLocalMeta existed = metas.next();
                if (meta.getUuid().equals(existed.getUuid())){
                    listOfHINCLocal.remove(existed);
                }
            }
            listOfHINCLocal.add(meta);
            AbstractDAO<HincLocalMeta> metaDAO = new AbstractDAO<>(HincLocalMeta.class);
            metaDAO.save(meta);
            logger.debug(" --> Add meta finished");
        } else {
            logger.debug(" --> No, it is not a SYN_REPLY message");
        }
        return null;// no need to reply
        */
    }
}
