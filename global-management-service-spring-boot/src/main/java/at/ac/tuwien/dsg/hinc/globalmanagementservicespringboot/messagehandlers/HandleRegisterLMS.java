package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.messagehandlers;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.model.LocalMS;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.HincLocalMetaRepository;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.LocalMSRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.metadata.HincLocalMeta;

@Component
public class HandleRegisterLMS extends HINCMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RabbitAdmin rabbitAdmin;
    private final HincLocalMetaRepository hincLocalMetaRepository;
    private final LocalMSRepository localMSRepository;

    @Value("${hinc.global.rabbitmq.output.broadcast}")
    private String outputBroadcast;

    @Value("${hinc.global.rabbitmq.output.groupcast}")
    private String outputGroupcast;

    @Value("${hinc.global.rabbitmq.output.unicast}")
    private String outputUnicast;


    @Autowired
    public HandleRegisterLMS(RabbitAdmin rabbitAdmin, HincLocalMetaRepository hincLocalMetaRepository, LocalMSRepository localMSRepository){
        super(HINCMessageType.REGISTER_LMS);

        this.rabbitAdmin = rabbitAdmin;
        this.hincLocalMetaRepository = hincLocalMetaRepository;
        this.localMSRepository = localMSRepository;
    }

    @Override
    protected HincMessage doHandle(HincMessage hincMessage) {
        logger.info("received " + hincMessage.toString());
        if(hincMessage.getPayload() == null) return null;

        LocalMS localMS = new LocalMS();

        HincLocalMeta meta = HincLocalMeta.fromJson(hincMessage.getPayload());
        localMS.setHincLocalMeta(meta);
        logger.info("  --> Meta: " + meta.toJson());
        hincLocalMetaRepository.save(meta);

        localMS.setGroup(meta.getGroupName());
        localMS.setId(meta.getUuid());

        String queue = localMS.getGroup() + "." + localMS.getId();
        this.declareBindings(queue, localMS.getGroup(), localMS.getId());

        return null;
    }


    private void declareBindings(String lmsQueue, String lmsGroup, String lmsID) {
        logger.info("binding broadcast exchange to "+lmsQueue);
        Binding broadcast = new Binding(lmsQueue, Binding.DestinationType.QUEUE, outputBroadcast, "", null);
        rabbitAdmin.declareBinding(broadcast);

        logger.info("binding groupcast exchange to "+lmsQueue);
        Binding groupcast = new Binding(lmsQueue, Binding.DestinationType.QUEUE, outputGroupcast, lmsGroup, null);
        rabbitAdmin.declareBinding(groupcast);

        logger.info("binding unicast exchange to "+lmsQueue);
        Binding unicast = new Binding(lmsQueue, Binding.DestinationType.QUEUE, outputUnicast, lmsID, null);
        rabbitAdmin.declareBinding(unicast);
    }
}
