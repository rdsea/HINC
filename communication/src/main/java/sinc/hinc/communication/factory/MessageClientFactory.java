/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.communication.factory;


import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.AMQPAdaptor.AMQPPublish;
import sinc.hinc.communication.AMQPAdaptor.AMQPSubscribe;
import sinc.hinc.communication.MQTTAdaptor.MQTTPublish;
import sinc.hinc.communication.MQTTAdaptor.MQTTSubscribe;

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
        this.broker = broker.trim();
        this.brokerType = brokerType.trim();
    }

    public static MessageClientFactory getFactory(String broker, String brokerType) {
        return new MessageClientFactory(broker, brokerType);
    }

    public MessagePublishInterface getMessagePublisher() {
        switch (getBrokerType().trim()) {
            case MessageClientFactory.MQTT:
                return new MQTTPublish(getBroker());
            case MessageClientFactory.AMQP:
                return new AMQPPublish(getBroker());            
            default:
                return null;
        }
    }

    public MessageSubscribeInterface getMessageSubscriber(HINCMessageHander handler) {
        switch (getBrokerType().trim()) {
            case MessageClientFactory.MQTT:
                return new MQTTSubscribe(getBroker(), handler);
            case MessageClientFactory.AMQP:
                return new AMQPSubscribe(getBroker(), handler);            
            default:
                System.out.println("Unknown BROKER_TYPE: " + getBrokerType());
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
