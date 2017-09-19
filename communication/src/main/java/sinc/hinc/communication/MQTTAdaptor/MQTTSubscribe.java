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
package sinc.hinc.communication.MQTTAdaptor;

import sinc.hinc.communication.factory.MessageSubscribeInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.communication.processing.HINCMessageHander;

/**
 *
 * @author Duc-Hung Le
 */
public class MQTTSubscribe extends MQTTConnector implements MessageSubscribeInterface {

    HINCMessageHander handler;

    public MQTTSubscribe(String broker, HINCMessageHander handling) {
        super(broker);
        this.handler = handling;
    }

    @Override
    public void subscribe(final String topic) {
        MqttCallback callBack = new MqttCallback() {

            @Override
            public void connectionLost(Throwable thrwbl) {
                logger.debug("MQTT is disconnected from topic: {}. Message: {}. Cause: {}", topic, thrwbl.getMessage(), thrwbl.getCause().getMessage());
                thrwbl.printStackTrace();
            }

            @Override
            public void messageArrived(String topic, MqttMessage mm) throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                HincMessage em = (HincMessage) mapper.readValue(mm.getPayload(), HincMessage.class);

                logger.debug("A message arrived. From: " + em.getSenderID() + ". MsgType: " + em.getMsgType() + ". Payload: " + em.getPayload());

                handler.handleMessage(em);
                //new Thread(new AsynHandleMessages(em)).start();
            }

            @Override

            public void deliveryComplete(IMqttDeliveryToken imdt) {
                logger.debug("Deliver complete to topic: " + topic);
            }
        };

        if (queueClient == null) {
            connect();
        }
        queueClient.setCallback(callBack);
        try {
            queueClient.subscribe(topic);
            logger.info("Subscribed the topic: " + topic);
        } catch (MqttException ex) {
            logger.error("Failed to subscribed to the topic: " + topic);
            ex.printStackTrace();
        }
    }

    @Override
    public void subscribe(String topic, long timeout) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class AsynHandleMessages implements Runnable {

        HincMessage em;

        AsynHandleMessages(HincMessage em) {
            logger.debug("Spawning a new thead to handle message {}", em.getPayload());
            this.em = em;
        }

        @Override
        public void run() {
            logger.debug("Pioneer is handingling message in an asyn thread: " + em.getPayload());
            handler.handleMessage(em);
        }

    }

}