/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.Extensible.reading;

import sinc.hinc.model.Extensible.ExtensibleModel;

/**
 *
 * @author hungld
 */
public class MQTTReading extends ExtensibleModel {

    String broker;
    String topic;

    public MQTTReading() {
        super(MQTTReading.class);
    }

    public MQTTReading(String broker, String topic) {
        super(MQTTReading.class);
        this.broker = broker;
        this.topic = topic;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
