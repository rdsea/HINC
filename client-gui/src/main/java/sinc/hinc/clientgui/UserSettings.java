/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.clientgui;

/**
 *
 * @author hungld
 */
public class UserSettings {

    public static String getBroker() {
        return "amqp://localhost";
    }

    public static String getBrokerType() {
        return "amqp";
    }

    public static String getUserName() {
        return "myClient";
    }
    
    public static String getDefaultEndpoint() {
        return "http://localhost:8888/global-management-service-1.0/rest";
    }

//
}
