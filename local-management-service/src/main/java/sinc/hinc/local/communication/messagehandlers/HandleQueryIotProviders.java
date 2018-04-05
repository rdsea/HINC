package sinc.hinc.local.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.local.communication.LocalCommunicationManager;
import sinc.hinc.local.plugin.AdaptorManager;


public class HandleQueryIotProviders extends HINCMessageHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalCommunicationManager localCommunicationManager;

    public HandleQueryIotProviders(LocalCommunicationManager localCommunicationManager) {
        super(HINCMessageType.QUERY_IOT_PROVIDERS);
        this.localCommunicationManager = localCommunicationManager;
    }

    @Override
    protected void doHandle(HincMessage hincMessage) {
        logger.debug("received " + hincMessage.toString());

        if (hincMessage.getPayload().contains("rescan")) {
            AdaptorManager.getInstance().scanAll();
        }

        int limit = -1;

    }
}
