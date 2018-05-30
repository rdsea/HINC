/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.utils.HincConfiguration;

/**
 *
 * @author hungld, linhsolar
 */

@SpringBootApplication
@EnableScheduling
@Configuration
public class LocalManagementService implements CommandLineRunner {

    public static void main(String[] args) {
        //DatabaseUtils.initDB();
        SpringApplication.run(LocalManagementService.class, args);
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    FanoutExchange hincOutputExchange;

    @Value("${hinc.local.group}")
    private String group;

    @Value("${hinc.local.id}")
    private String id;

    @Override
    public void run(String... args) throws Exception {
        HincMessage registerMessage = new HincMessage();
        registerMessage.setSenderID(id);

        HincLocalMeta meta = new HincLocalMeta();
        meta.setGroupName(group);
        meta.setUuid(id);

        registerMessage.setPayload(meta.toJson());
        registerMessage.setMsgType(HINCMessageType.REGISTER_LMS);

        logger.info("sending register message for "+ group+"."+id);
        logger.info(meta.toJson());
        rabbitTemplate.send(hincOutputExchange.getName(), "", new Message(registerMessage.toJson().getBytes(), new MessageProperties()));
    }
}
