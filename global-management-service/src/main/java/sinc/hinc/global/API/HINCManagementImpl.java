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
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.GlobalCommunicationManager;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author hungld
 */
//@Path("/")
public class HINCManagementImpl implements HINCManagementAPI {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static HINCGlobalMeta meta;
    private List<HincLocalMeta> listOfHINCLocal = new ArrayList<>();
    private GlobalCommunicationManager globalCommunicationManager;

    /*private static HINCMessageListener listener = null;
    private HINCMessageSender comMng = getCommunicationManager();*/

    @PostConstruct
    public void init() {
        logger.debug("Init HINCManagementImpl");
        logger.debug("Get GlobalCommunicationManager");

        globalCommunicationManager = GlobalCommunicationManager.getInstance();
        GlobalCommunicationManager.initialize("localhost");

        /*TODO remove old code
        if (listener == null) {
            listener = new HINCMessageListener(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
            listener.addListener(HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), HINCMessageType.UPDATE_INFORMATION_SINGLEIOTUNIT.toString(), new SingleIoTUnitUpdateHandler());
            listener.addListener(HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), HINCMessageType.SYN_REPLY.toString(), new HincLocalSyncHandler(logger, this.listOfHINCLocal));
            listener.listen();
        }*/
    }

    /*public HINCMessageSender getCommunicationManager() {
        if (comMng == null) {
            comMng = new HINCMessageSender(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
        }
        return this.comMng;
    }*/

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
        //comMng = new HINCMessageSender(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
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
        //comMng.asynCall(timeout, discoveringMessage, new HincLocalSyncHandler(logger, listOfHINCLocal));
        //TODO make async call work
        try {
            globalCommunicationManager.broadcastMessage(discoveringMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        String group = localUUID.split("/")[0];
        String lmsId = localUUID.split("/")[1];
        //TODO check if another messagehandler is necessary (because old code uses a seperate messagehandler)
        try {
            globalCommunicationManager.unicastMessage(group, lmsId, updateRequestMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*comMng.asynCall(0, updateRequestMsg, new HINCMessageHander() {
            @Override
            public HincMessage handleMessage(HincMessage message) {
                logger.debug("The SYNC message for updating name space should be received. The sync listener will process it." + message.toJson());
                return null;
            }
        });  */
    }

}
