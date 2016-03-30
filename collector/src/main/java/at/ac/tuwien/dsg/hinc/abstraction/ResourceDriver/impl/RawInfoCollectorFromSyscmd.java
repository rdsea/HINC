/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.impl;


import at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.InfoSourceSettings;
import at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.RawInfoCollector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hungld
 */
public class RawInfoCollectorFromSyscmd implements RawInfoCollector {

    @Override
    public Map<String, String> getRawInformation(InfoSourceSettings.InfoSource infoSource) {
        String cmd = infoSource.getEndpoint();

        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            Map<String, String> map = new HashMap<>();
            map.put(cmd, sb.toString());
            return map;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            return null;
        }

    }

}
