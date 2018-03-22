/*
 * Copyright (c) 2013 Technische Universitat Wien (TUW), Distributed Systems Group. http://dsg.tuwien.ac.at
 *
 * This work was partially supported by the European Commission in terms of the CELAR FP7 project (FP7-ICT-2011-8 #317790), http://www.celarcloud.eu/
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package sinc.hinc.communication.AMQPAdaptor;

import sinc.hinc.communication.factory.MessagePublishInterface;

import com.rabbitmq.client.ConsumerCancelledException;
//import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.common.metadata.HincMessageTopic;

/**
 *
 * @author Duc-Hung Le
 */
//TODO adjust to new Messaging Design, remove unused functions
public class AMQPPublish extends AMQPConnector implements MessagePublishInterface {

    public AMQPPublish() {
    }

    public AMQPPublish(String broker) {
        super(broker);
    }

    @Override
    public void pushMessage(HincMessage content) {
        connect();

        logger.debug("Start publishing message {} on topic {}, stamp/uuid: {}/{}", content.getMsgType(), content.getTopic(), content.getTimeStamp(), content.getUuid());
        if (content.getPayload() != null && content.getPayload().length() < 2048) {
            logger.debug("Content: " + content);
        }
        try {
            String EXCHANGE_NAME = content.getTopic();
            logger.debug("  --> EXCHANGE_NAME: " + EXCHANGE_NAME);
            amqpChannel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            amqpChannel.basicPublish(EXCHANGE_NAME, "", null, content.toJson().getBytes());
        } catch (IOException me) {
            logger.debug("  --> Message {} is FAILED to publish on topic {} ", content.getMsgType(), content.getTopic());
            logger.debug("  --> msg " + me.getMessage());
            logger.debug("  -->loc " + me.getLocalizedMessage());
            logger.debug("  --> excep " + me);
            me.printStackTrace();
        }
        logger.debug("  --> Message {} is published on topic {} ", content.getMsgType(), content.getTopic());
//        disconnect();

    }

    @Override
    public HincMessage callFunction(HincMessage content) {
        /*logger.debug("Calling function by message: " + content.toJson());

        connect();

        try {
            // this actively declare a server-named exclusive, autodelete, non-durable queue.
            // this is just for the reply purpose
            String replyTopicName = HincMessageTopic.getTemporaryTopic();
            logger.debug("Reply topic: " + replyTopicName);

            // first publish the request message to call function            
            String EXCHANGE_NAME = content.getTopic();
            content.setFeedbackTopic(replyTopicName);
            amqpChannel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            amqpChannel.basicPublish(EXCHANGE_NAME, "", null, content.toJson().getBytes());

            logger.debug("Publish request for calling function done ! Now consume queue to wait for the result");

            // then consume to the replyTopicName topic, that wait for the reply
            // setup the queue name in the channel
            amqpChannel.exchangeDeclare(replyTopicName, "fanout");
            String queueName = amqpChannel.queueDeclare().getQueue();
            amqpChannel.queueBind(queueName, replyTopicName, "");

            QueueingConsumer consumer = new QueueingConsumer(amqpChannel);
            amqpChannel.basicConsume(queueName, true, consumer);

            // note: this will block at nextDelivery
            // TODO: give timeout function
            logger.debug("Waiting for the message in topic: " + replyTopicName);
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            logger.debug("Got it, the msg in topic: " + replyTopicName);
            String response = new String(delivery.getBody());
            HincMessage responseMsg = HincMessage.fromJson(response);
            //logger.debug("Response string: " + responseMsg.toJson());
            return responseMsg;

        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Error when publish message: " + ex.getMessage());
        } catch (InterruptedException | ShutdownSignalException | ConsumerCancelledException ex) {
            ex.printStackTrace();
            logger.error("Error when waiting for the reply: " + ex.getMessage());
        }
        */
        return null;
    }

    public void pushMessageAndDisconnect(HincMessage content) {
        pushMessage(content);
//        disconnect();
    }

    public void pushCustomData(String content, String topic) {
        try {
            if (amqpChannel == null) {
                connect();
            }
            logger.debug("Publishing custom data: " + content);
            amqpChannel.exchangeDeclare(topic, "fanout");
            amqpChannel.basicPublish(topic, "", null, content.getBytes());
            logger.debug("Message published");
        } catch (IOException me) {
            logger.debug(" --> msg " + me.getMessage());
            logger.debug(" --> loc " + me.getLocalizedMessage());
            logger.debug(" --> excep " + me);
            me.printStackTrace();
        }
    }

}
