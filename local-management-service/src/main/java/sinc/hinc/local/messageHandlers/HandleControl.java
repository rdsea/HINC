/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.executors.LocalExecutor;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

/**
 *
 * @author hungld
 */
public class HandleControl implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage message) {
        String controlPointUUID = message.getPayload();
        AbstractDAO<ControlPoint> cpDAO = new AbstractDAO<>(ControlPoint.class);
        ControlPoint cp = cpDAO.read(controlPointUUID);
        String result;
        if (cp!=null){
            switch (cp.getInvokeProtocol()){
                case LOCAL_EXECUTE:
                    cp.setParameters(message.getExtra().get("param"));                    
                    result = LocalExecutor.execute(cp);                    
                    break;
                default:
                    result="Method " + cp.getInvokeProtocol() + " in the control point is not supported to execute yet !";
                    break;
            }
        } else {
            result = "Cannot find control point with ID: " + controlPointUUID;
        }
        return new HincMessage(HINCMessageType.CONTROL_RESULT.toString(), HincConfiguration.getMyUUID(),message.getFeedbackTopic(),"", result);      
//        MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
//        FACTORY.getMessagePublisher().pushMessage(replyMsg);
//        return replyMsg;
    }
    
}
