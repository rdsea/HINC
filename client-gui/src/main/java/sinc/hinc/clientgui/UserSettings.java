/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.clientgui;

import sinc.hinc.global.management.CommunicationManager;

/**
 *
 * @author hungld
 */
public class UserSettings {

    public static String getBroker() {
        return "amqp://128.130.172.215";
    }

    public static String getBrokerType() {
        return "amqp";
    }

    public static String getUserName() {
        return "myClient";
    }

    public static CommunicationManager getQueryManager() {
        return new CommunicationManager(getUserName(), getBroker(), getBrokerType());
    }
}
