/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.API;

import com.orientechnologies.orient.core.record.impl.ODocument;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.API.HINCManagementAPI;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.metadata.HincMessage;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.messageInterface.SalsaMessageHandling;
import sinc.hinc.global.management.CommunicationManager;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

/**
 *
 * @author hungld
 */
//@Path("/")
public class HINCManagementImpl implements HINCManagementAPI{
    
    static HINCGlobalMeta meta;
    CommunicationManager comMng = getCommunicationManager();
    static Logger logger = LoggerFactory.getLogger("HINC");
    List<HincLocalMeta> listOfHINCLocal = new ArrayList<>();
    
    public CommunicationManager getCommunicationManager() {
        if (comMng == null) {
            this.comMng = new CommunicationManager(HincConfiguration.getGroupName(), HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
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
        this.comMng = new CommunicationManager(meta.getGroup(), meta.getBroker(), meta.getBrokerType());
    }

    @Override
    public List<HincLocalMeta> queryHINCLocal(int timeout) {
        logger.debug("Start syn HINCLocal...");
        AbstractDAO<HincLocalMeta> metaDAO = new AbstractDAO<>(HincLocalMeta.class);
        if (timeout == 0) {
            List<HincLocalMeta> metas = metaDAO.readAll();
            return metas;
        }
        listOfHINCLocal.clear();
        comMng.synFunctionCallBroadcast(timeout, HincMessageTopic.REGISTER_AND_HEARBEAT, HincMessage.MESSAGE_TYPE.SYN_REQUEST, HincMessageTopic.CLIENT_REQUEST_HINC, new SalsaMessageHandling() {
            @Override
            public void handleMessage(HincMessage msg) {
                logger.debug("A message arrive, from: {}, type: {}, topic: {} ", msg.getFromSalsa(), msg.getMsgType(), msg.getTopic());
                if (msg.getMsgType().equals(HincMessage.MESSAGE_TYPE.SYN_REPLY)) {
                    logger.debug("Yes, it is a SYN message, adding the metadata");
                    HincLocalMeta meta = HincLocalMeta.fromJson(msg.getPayload());
                    logger.debug("Meta: " + meta.toJson());
                    listOfHINCLocal.add(meta);
                }
                logger.debug("Add meta finished");
            }
        });

        logger.debug("Done, should close the subscribe now.. ");

        // write to DB        
        metaDAO.deleteAll();
        List<ODocument> result = metaDAO.saveAll(listOfHINCLocal);
        logger.debug("Saving to DB is also done ! Result are: ");
        for(ODocument o: result){
            logger.debug(o.toJSON() + "\n");
        }

        return listOfHINCLocal;
    }
}
