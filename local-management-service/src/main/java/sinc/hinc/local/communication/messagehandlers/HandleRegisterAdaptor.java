package sinc.hinc.local.communication.messagehandlers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.local.plugin.AdaptorManager;

import java.io.IOException;
@Component
public class HandleRegisterAdaptor extends HINCMessageHandler{

    public HandleRegisterAdaptor() {
        super(HINCMessageType.REGISTER_ADAPTOR);
    }

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private AdaptorManager adaptorManager;

    @Value("${adaptor.amqp.output.broadcast}")
    private String adaptorOutputBroadcastExchange;

    @Value("${adaptor.amqp.output.unicast}")
    private String adaptorOutputUnicastExchange;

    @Override
    protected HincMessage doHandle(HincMessage hincMessage) {
        try {
            JsonNode node = this.objectMapper.readTree(hincMessage.getPayload());
            String adaptorName = node.get("adaptorName").textValue();
            adaptorManager.addAdaptor(adaptorName);

            // declare bindings
            logger.info("creating broadcast binding for "+adaptorName);
            Binding broadcast = new Binding(adaptorName, Binding.DestinationType.QUEUE, adaptorOutputBroadcastExchange, "", null);
            rabbitAdmin.declareBinding(broadcast);

            logger.info("creating unicast binding for "+adaptorName);
            Binding unicast = new Binding(adaptorName, Binding.DestinationType.QUEUE, adaptorOutputUnicastExchange, adaptorName, null);
            rabbitAdmin.declareBinding(unicast);

        } catch (IOException e) {
            logger.error("failed to serialize payload "+hincMessage.getMsgType());
            e.printStackTrace();
        }
        return null;
    }
}
