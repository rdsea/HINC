package sinc.hinc.local.communication.messagehandlers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.local.plugin.AdaptorManager;

import java.io.IOException;
@Component
public class HandleDeregisterAdaptor extends HINCMessageHandler {
    @Autowired
    private AdaptorManager adaptorManager;

    public HandleDeregisterAdaptor() {
        super(HINCMessageType.DEREGISTER_ADAPTOR);
    }

    @Override
    protected HincMessage doHandle(HincMessage hincMessage) {
        try {
            JsonNode node = this.objectMapper.readTree(hincMessage.getPayload());
            String adaptorName = node.get("adaptorName").textValue();
            adaptorManager.removeAdaptor(adaptorName);
        } catch (IOException e) {
            logger.error("failed to serialize payload "+hincMessage.getMsgType());
            e.printStackTrace();
        }

        return null;
    }
}
