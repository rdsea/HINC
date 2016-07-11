/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import sinc.hinc.abstraction.transformer.ControlPointTransformer;
import sinc.hinc.abstraction.transformer.DataPointTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;

/**
 *
 * @author hungld
 */
public class TeitSensorTransformer implements DataPointTransformer<Properties>, ControlPointTransformer<Properties> {

    @Override
    public DataPoint updateDataPoint(Properties prop) {
        DataPoint dp = new DataPoint();
        dp.setName(prop.getProperty(""));
        dp.setMeasurementUnit("unknown");
        dp.setDescription("Virtual sensor");
        dp.setDatatype(prop.getProperty("unknown"));
        dp.setResourceID(prop.getProperty("sensorID"));
        String apiName = mapApiName(prop.getProperty("platform"));
        dp.setDataApi(apiName);
        Map<String, String> dataApiSettings = new HashMap<>();
        for (Entry<Object, Object> entry : prop.entrySet()) {
            String key = entry.getKey().toString().trim();
            if (key.startsWith("platform." + apiName)) {
                String shortKey = key.substring(key.lastIndexOf(".") + 1).trim();
                dataApiSettings.put(shortKey, entry.getValue().toString().trim());
            }
        }
        dp.setDataApiSettings(dataApiSettings);
        return dp;
    }

    @Override
    public String getName() {
        return "teit";
    }

    private String mapApiName(String className) {
        switch (className) {
            case "teit.sensor.PlatformConsole.ConsolePlatform":
                return "console";
            case "teit.sensor.PlatformSparkFun.SparkfunPlatform":
                return "sparkfun";
            case "teit.sensor.PlatformThingSpeak.ThingSpeakPlatform":
                return "thingspeak";
            case "teit.sensor.MQTT.MQTTOutput":
                return "mqtt";
            default:
                return "unknown";
        }
    }

    @Override
    public List<ControlPoint> updateControlPoint(Properties data) {
        String sensorScript = data.getProperty("executionscript");
        
        List<ControlPoint> cps = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("bash " + sensorScript + " list-commands");

            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            System.out.println("Execute command: " + "bash " + sensorScript + " list-commands");
            while ((line = reader.readLine()) != null) {
                System.out.println("Adding controlpoint from: "+line);
                line = line.trim() + " ";
                ControlPoint cp = new ControlPoint(data.getProperty("sensorID"), line.substring(0, line.indexOf(" ")), line.substring(0, line.indexOf(" ")) + " the virtual sensor", ControlPoint.InvokeProtocol.LOCAL_EXECUTE, sensorScript + " " + line.trim());
                System.out.println("  --> Controlpoint added: " + cp.getReference());
                cps.add(cp);
            }
            return cps;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            return cps;
        }
    }

}
