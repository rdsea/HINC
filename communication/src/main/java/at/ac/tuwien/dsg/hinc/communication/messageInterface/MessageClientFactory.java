/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.communication.messageInterface;


import at.ac.tuwien.dsg.hinc.communication.AMQPAdaptor.AMQPPublish;
import at.ac.tuwien.dsg.hinc.communication.AMQPAdaptor.AMQPSubscribe;
import at.ac.tuwien.dsg.hinc.communication.MQTTAdaptor.MQTTPublish;
import at.ac.tuwien.dsg.hinc.communication.MQTTAdaptor.MQTTSubscribe;

/**
 *
 * @author Duc-Hung LE
 * Modified by Hong-Linh Truong
 */
public class MessageClientFactory {

	
	//TODO: move static members to a global file
	public final static String MQTT="mqtt";
    public final static String AMQP ="amqp";
	String broker;
    String brokerType;

    String exportBroker;
    String exportBrokerType;

    public MessageClientFactory(String broker, String brokerType) {
        this.broker = broker;
        this.brokerType = brokerType;
    }

    public static MessageClientFactory getFactory(String broker, String brokerType) {
        return new MessageClientFactory(broker, brokerType);
    }

    public MessagePublishInterface getMessagePublisher() {
        switch (getBrokerType()) {
            case MessageClientFactory.MQTT:
                return new MQTTPublish(getBroker());
            case MessageClientFactory.AMQP:
                return new AMQPPublish(getBroker());            
            default:
                return null;
        }
    }

    public MessageSubscribeInterface getMessageSubscriber(SalsaMessageHandling handler) {
        switch (getBrokerType()) {
            case MessageClientFactory.MQTT:
                return new MQTTSubscribe(getBroker(), handler);
            case MessageClientFactory.AMQP:
                return new AMQPSubscribe(getBroker(), handler);            
            default:
                return null;
        }
    }

    public String getBroker() {
        return broker;
    }

    public String getBrokerType() {
        return brokerType;
    }
    
}
