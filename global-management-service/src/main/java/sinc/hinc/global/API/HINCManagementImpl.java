/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.API;

import com.orientechnologies.orient.core.record.impl.ODocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.API.HINCManagementAPI;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HINCMessageSender;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import sinc.hinc.apps.guibeans.handlers.HincLocalSyncHandler;
import sinc.hinc.apps.guibeans.handlers.SingleIoTUnitUpdateHandler;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HINCMessageListener;

/**
 * @author hungld
 */
//@Path("/")
public class HINCManagementImpl implements HINCManagementAPI {

    static HINCGlobalMeta meta;
    static Logger logger = LoggerFactory.getLogger("HINC");
    HINCMessageSender comMng = getCommunicationManager();
    List<HincLocalMeta> listOfHINCLocal = new ArrayList<>();

    static HINCMessageListener listener = null;

    @PostConstruct
    public void init() {
        if (listener == null) {
            logger.debug("Start to listen to the UPDATE_INFORMATION message from the group topic");
            listener = new HINCMessageListener(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
            listener.addListener(HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), HINCMessageType.UPDATE_INFORMATION.toString(), new SingleIoTUnitUpdateHandler());
            listener.addListener(HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), HINCMessageType.SYN_REPLY.toString(), new HincLocalSyncHandler(logger, this.listOfHINCLocal));
            listener.listen();
        }
    }

    public HINCMessageSender getCommunicationManager() {
        if (comMng == null) {
            comMng = new HINCMessageSender(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
        }
        return this.comMng;
    }

    @Override
    public HINCGlobalMeta getHINCGlobalMeta() {
        if (meta == null) {
            meta = HincConfiguration.getGlobalMeta();
            return meta;
        }
        return meta;
    }

    @Override
    public void setHINCGlobalMeta(HINCGlobalMeta metaInfo) {
        meta = metaInfo;
        comMng = new HINCMessageSender(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
    }

    @Override
    public List<HincLocalMeta> queryHINCLocal(int timeout) {
        logger.debug("Start syn HINCLocal...");
        AbstractDAO<HincLocalMeta> metaDAO = new AbstractDAO<>(HincLocalMeta.class);
        if (timeout == 0) {
            logger.debug("Timeout = 0, read HINC entity in local DB, not request is sent");
            List<HincLocalMeta> metas = metaDAO.readAll();
            return metas;
        }
        listOfHINCLocal.clear();
        HincMessage discoveringMessage = new HincMessage(HINCMessageType.SYN_REQUEST.toString(), HincConfiguration.getMyUUID(), HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), HincMessageTopic.getTemporaryTopic(), "");
        comMng.asynCall(timeout, discoveringMessage, new HINCMessageHander() {
            @Override
            public HincMessage handleMessage(HincMessage msg) {
                logger.debug("A message arrive, from: {}, type: {}, topic: {} ", msg.getSenderID(), msg.getMsgType(), msg.getTopic());
                if (msg.getMsgType().equals(HINCMessageType.SYN_REPLY.toString())) { // this will be always true !!
                    logger.debug(" --> Yes, it is a SYN_REPLY message, adding the metadata");
                    HincLocalMeta meta = HincLocalMeta.fromJson(msg.getPayload());
                    logger.debug("  --> Meta: " + meta.toJson());
                    listOfHINCLocal.add(meta);
                    logger.debug(" --> Add meta finished");
                } else {
                    logger.debug(" --> No, it is not a SYN_REPLY message");
                }
                return null;// no need to reply
            }
        });

        logger.debug(" --> Waiting for HINC Local is done, should close the subscribe now.. ");

        // write to DB        
        metaDAO.deleteAll();
        List<ODocument> result = metaDAO.saveAll(listOfHINCLocal);
        logger.debug(" --> Saving to DB is also done ! Result are: ");
        for (ODocument o : result) {
            logger.debug(o.toJSON() + "\n");
        }

        return listOfHINCLocal;
    }

    @Override
    public void setHINCLocalTopic(String infoBase, String localUUID, String append) {

        HincMessage updateRequestMsg = new HincMessage(HINCMessageType.UPDATE_INFO_BASE.toString(), HincConfiguration.getMyUUID(), HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), HincMessageTopic.getTemporaryTopic(), infoBase);
        updateRequestMsg.setReceiverID(localUUID);
        if (append.equals("true")) {
            updateRequestMsg.hasExtra("append", "true");
        }
        comMng.asynCall(0, updateRequestMsg, new HINCMessageHander() {
            @Override
            public HincMessage handleMessage(HincMessage message) {
                logger.debug("The SYNC message for updating name space should be received. The sync listener will process it." + message.toJson());
                return null;
            }
        });      
    }

}
