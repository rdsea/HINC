package sinc.hinc.local.communication.messagehandlers;

import com.fasterxml.jackson.databind.JsonNode;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.local.plugin.AdaptorManager;

import java.io.IOException;

public class HandleDeregisterAdaptor extends HINCMessageHandler {
    public HandleDeregisterAdaptor() {
        super(HINCMessageType.DEREGISTER_ADAPTOR);
    }

    @Override
    protected void doHandle(HincMessage hincMessage) {
        try {
            JsonNode node = this.objectMapper.readTree(hincMessage.getPayload());
            String adaptorName = node.get("adaptorName").textValue();
            AdaptorManager.getInstance().removeAdaptor(adaptorName);
        } catch (IOException e) {
            logger.error("failed to serialize payload "+hincMessage.getMsgType());
            e.printStackTrace();
        }
    }
}
