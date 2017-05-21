/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.apps.guibeans.handlers;

import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;

/**
 * Process sync message from HINC local send to.
 * 
 * This handler extracts the HincLocalMeta and update the list.
 * ListOfHINCLocal is an external place to store HINC Local metadata
 * 
 * @author hungld
 */
public class HincLocalSyncHandler implements HINCMessageHander {

    Logger logger;
    List<HincLocalMeta> listOfHINCLocal;

    public HincLocalSyncHandler(Logger logger, List<HincLocalMeta> listOfHINCLocal) {
        this.logger = logger;
        this.listOfHINCLocal = listOfHINCLocal;
    }

    @Override
    public HincMessage handleMessage(HincMessage msg) {
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
            logger.debug(" --> Add meta finished");
        } else {
            logger.debug(" --> No, it is not a SYN_REPLY message");
        }
        return null;// no need to reply
    }

}
