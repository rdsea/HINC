package sinc.hinc.local.communication.messagehandlers;

import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.payloads.ControlResult;

import java.io.IOException;

public class HandleControlResult extends HINCMessageHandler{
    public HandleControlResult() {
        super(HINCMessageType.CONTROL_RESULT);
    }

    @Override
    protected void doHandle(HincMessage hincMessage) {
        try {
            ControlResult controlResult = this.objectMapper.readValue(hincMessage.getPayload(), ControlResult.class);
            logger.info("control result received "+hincMessage.getPayload());
            // TODO we have to forward this back to the origin
            // or save this in the DB to be queried by the client
        } catch (IOException e) {
            this.logger.error("failed to handle message "+this.messageType);
            e.printStackTrace();
        }
    }
}
