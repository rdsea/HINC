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

    static String ip = "localhost";
    static String port = "8888";

    public static String getHINCGlobalRESTEndpoint() {
        return "http://" + ip + ":" + port + "/global-management-service-1.0/rest";
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        UserSettings.ip = ip;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        UserSettings.port = port;
    }
    
    

}
