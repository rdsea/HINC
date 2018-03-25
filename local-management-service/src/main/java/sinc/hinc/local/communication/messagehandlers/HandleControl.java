package sinc.hinc.local.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.ProviderControlResult;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.payloads.ControlBody;
import sinc.hinc.communication.payloads.ControlResult;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.LocalManagementService;
import sinc.hinc.local.communication.LocalCommunicationManager;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

public class HandleControl implements IMessageHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalCommunicationManager localCommunicationManager;

    public HandleControl(LocalCommunicationManager localCommunicationManager) {
        this.localCommunicationManager = localCommunicationManager;
    }

    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.CONTROL;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        logger.debug("received " + hincMessage.toString());
        //TODO implement MessageHandler

        // marshall the parameters and options provided by the user
        ControlBody body = ControlBody.fromJson(hincMessage.getPayload());
        ProviderQueryAdaptor adaptor = LocalManagementService.pluginManager.getAdaptorByName(body.getAdaptorName());

        String controlPointUUID = body.getControlPointUUID();
        AbstractDAO<ControlPoint> controlPointDAO = new AbstractDAO<>(ControlPoint.class);
        ControlPoint controlPoint = controlPointDAO.read(controlPointUUID);
        ProviderControlResult controlResult = adaptor.sendControl(controlPoint);

        ControlResult result = new ControlResult();
        result.setResult(ControlResult.CONTROL_RESULT.valueOf(controlResult.getResult().name()));
        result.setOutput(controlResult.getOutput());
        result.setExitcode(controlResult.getExitcode());
        result.setExecutionTime(controlResult.getExecutionTime());
        result.setUpdateIoTUnit(controlResult.getUpdateIoTUnit());

        HincMessage reply = new HincMessage(HINCMessageType.CONTROL_RESULT.toString(), HincConfiguration.getMyUUID(), hincMessage.getFeedbackTopic(), "", result.toJson());
        localCommunicationManager.sendToGlobal(reply);
    }
}
