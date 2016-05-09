/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.common.metadata;

/**
 *
 * @author hungld
 */
public class HINCGlobalMeta {

    String group;
    String broker;
    String brokerType;

    public HINCGlobalMeta() {
    }

    public HINCGlobalMeta(String group, String broker, String brokerType) {

        this.group = group;
        this.broker = broker;
        this.brokerType = brokerType;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getBrokerType() {
        return brokerType;
    }

    public void setBrokerType(String brokerType) {
        this.brokerType = brokerType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
