package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.messagehandlers;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.HincLocalMetaRepository;
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
public class HandleSynReply extends HINCMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RabbitAdmin rabbitAdmin;
    private final HincLocalMetaRepository hincLocalMetaRepository;

    @Value("${hinc.global.rabbitmq.output.broadcast}")
    private String outputBroadcast;

    @Value("${hinc.global.rabbitmq.output.groupcast}")
    private String outputGroupcast;

    @Value("${hinc.global.rabbitmq.output.unicast}")
    private String outputUnicast;


    @Autowired
    public HandleSynReply(RabbitAdmin rabbitAdmin, HincLocalMetaRepository hincLocalMetaRepository){
        super(HINCMessageType.SYN_REPLY);

        this.rabbitAdmin = rabbitAdmin;
        this.hincLocalMetaRepository = hincLocalMetaRepository;
    }

    @Override
    protected void doHandle(HincMessage hincMessage) {
        logger.debug("received " + hincMessage.toString());

        //TODO improve implementation --> hincMessage properties

        String group = hincMessage.getDestination().getExchange();
        String id = hincMessage.getSenderID();


        if(id!=null && group != null) {
            String queue = group + "." + id;

            this.addLocalManagementService(queue, group, id);


            if(hincMessage.getPayload() != null) {
                HincLocalMeta meta = HincLocalMeta.fromJson(hincMessage.getPayload());
                logger.debug("  --> Meta: " + meta.toJson());
                hincLocalMetaRepository.save(meta);
            }
        }
    }


    private void addLocalManagementService(String lmsQueue, String lmsGroup, String lmsID) {
        Binding broadcast = new Binding(lmsQueue, Binding.DestinationType.QUEUE, outputBroadcast, "", null);
        rabbitAdmin.declareBinding(broadcast);

        Binding groupcast = new Binding(lmsQueue, Binding.DestinationType.QUEUE, outputGroupcast, lmsGroup, null);
        rabbitAdmin.declareBinding(groupcast);

        Binding unicast = new Binding(lmsQueue, Binding.DestinationType.QUEUE, outputUnicast, lmsGroup+"."+lmsID, null);
        rabbitAdmin.declareBinding(unicast);
    }
}
