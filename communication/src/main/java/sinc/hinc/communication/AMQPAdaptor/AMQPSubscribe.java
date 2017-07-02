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

import sinc.hinc.communication.factory.MessageSubscribeInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.communication.processing.HINCMessageHander;

/**
 *
 * @author Duc-Hung Le
 */
public class AMQPSubscribe extends AMQPConnector implements MessageSubscribeInterface {

    HINCMessageHander handler;

    public AMQPSubscribe(String broker, HINCMessageHander handling) {
        super(broker);
        this.handler = handling;
    }

    @Override
    public void subscribe(String topic, long timeout) {
        if (topic == null) {
            logger.error("Subscribing to an empty topic... ");
            return;
        }
        if (amqpChannel == null) {
            connect();
        }
        try {
            amqpChannel.exchangeDeclare(topic, "fanout");
            String queueName = amqpChannel.queueDeclare().getQueue();
            amqpChannel.queueBind(queueName, topic, "");

            QueueingConsumer consumer = new QueueingConsumer(amqpChannel);
            amqpChannel.basicConsume(queueName, true, consumer);
            System.out.println("AMQP Subscribed. Exchange name: " + topic + ", queue name: " + queueName);
            new Thread(new ThreadQueueSubscribe(consumer, handler, timeout)).start();
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Cannot subscribe to topic: {}", topic, ex);
        } catch (ShutdownSignalException | ConsumerCancelledException ex) {
            ex.printStackTrace();
            logger.error("Interrupt during the subscribing to topic: {}", topic, ex);
        }
    }

    @Override
    public void subscribe(String topic) {
        subscribe(topic, 0);
    }

    private class ThreadQueueSubscribe implements Runnable {

        HINCMessageHander handler;
        QueueingConsumer consumer;
        String topic;
        long timeout;
        long startTime;

        ThreadQueueSubscribe(QueueingConsumer consumer, HINCMessageHander handler, long timeout) {
            this.handler = handler;
            this.consumer = consumer;;
            this.timeout = timeout; // in miliseconds
            this.startTime = (new Date()).getTime();
        }

        @Override
        public void run() {
            //logger.debug("Inside the queue subscribing thread, process is continueing...");
            try {
                while (true) {

                    logger.debug("Looping and waiting for the message, timeout: " + timeout);

                    QueueingConsumer.Delivery delivery;
                    if (timeout == 0) {
                        delivery = consumer.nextDelivery();
                    } else {
                        delivery = consumer.nextDelivery(timeout);
                    }

                    if (delivery == null) {
                        logger.debug("It seems to be timeout, so delivery is null...");
                        break;
                    }

                    String mm = new String(delivery.getBody());

                    ObjectMapper mapper = new ObjectMapper();
                    HincMessage em = (HincMessage) mapper.readValue(mm, HincMessage.class);
                    this.topic = em.getTopic();
                    //logger.debug("A message arrived. From: " + em.getFromSalsa() + ". MsgType: " + em.getMsgType() + ". Payload: " + em.getPayload());
                    logger.debug("A message arrived. From: " + em.getSenderID() + ". MsgType: " + em.getMsgType() + ". Stamp/UUID: " + em.getTimeStamp() + "/" + em.getUuid());
                    new Thread(new HandlingThread(handler, em)).start();
                    logger.debug("If handle message done, it must exit and show this, topic: " + topic);
                    // quit if timeout
                    if (timeout > 0) {
                        logger.debug("YES, timeout > 0");
                        long currentTime = (new Date()).getTime();
                        logger.debug("Miliseconds left before unsubscribing: " + currentTime + ", topic: " + topic);
                        if (currentTime - startTime > timeout) {
                            logger.debug("RETURN BECAUSE OF TIMEOUT !");
                            break;
                        }
                    }

                }
                logger.debug("The loop that wait the message is over ! Topic: " + topic);
//                consumer.getChannel().getConnection().close();
                logger.debug("ThreadQueueSubscribe should exit here ! Topic: " + topic);
            } catch (IOException ex) {
                ex.printStackTrace();
                logger.error("Cannot subscribe to topic: {}", topic, ex);
            } catch (InterruptedException | ShutdownSignalException | ConsumerCancelledException ex) {
                ex.printStackTrace();
                logger.error("Interrupt during the subscribing to topic: {}", topic, ex);
            }

        }
    }

    private class HandlingThread implements Runnable {

        HINCMessageHander handler;
        HincMessage em;

        HandlingThread(HINCMessageHander handler, HincMessage em) {
            this.handler = handler;
            this.em = em;
        }

        @Override
        public void run() {
            //logger.debug("Inside the handling thread, process is continueing...");
            this.handler.handleMessage(em);
            //logger.debug("Handle message done !");
        }

    }

}
