/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers.control;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.payloads.ControlResult;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.executors.ExecutorsInterface;
import sinc.hinc.local.executors.LocalExecutor;
import sinc.hinc.local.executors.LocalAsyncExecutor;
import sinc.hinc.local.messageHandlers.control.invokers.Invoker;
import sinc.hinc.local.messageHandlers.control.invokers.PostInvoker;
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
        ControlResult result;
        if (cp != null) {
            switch (cp.getInvokeProtocol()) {
                case LOCAL_EXECUTE:
                    cp.setParameters(message.getExtra().get("param"));
                    LocalExecutor executor = new LocalExecutor();
                    result = executor.execute(cp);
                    break;
                case LOCAL_ASYNC_EXECUTE:
                    if(message.getExtra().get("param")!=null){
                        cp.setParameters(message.getExtra().get("param"));
                    }
                    ExecutorsInterface executorsInterface = new LocalAsyncExecutor();
                    result = executorsInterface.execute(cp);
                    break;
                case POST:
                    cp.setParameters(message.getExtra().get("param"));
                    Invoker invoker = new PostInvoker();
                    result = invoker.invoke(cp);
                    break;
                default:
                    result = new ControlResult(ControlResult.CONTROL_RESULT.EXECUTOR_NOT_SUPPORT, 0, "Method " + cp.getInvokeProtocol() + " in the control point is not supported to execute yet !");
                    break;
            }
        } else {
            System.out.println("Do not find control point id: " + controlPointUUID);
            return null;
            // result = new ControlResult(ControlResult.CONTROL_RESULT.COMMAND_NOT_FOUND, 0, "Cannot find control point with ID: " + controlPointUUID);
        }

        return new HincMessage(HINCMessageType.CONTROL_RESULT.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", result.toJson());
    }

}
