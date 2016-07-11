/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.executors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

/**
 *
 * @author hungld
 */
public class LocalExecutor {
    static Logger logger = HincConfiguration.getLogger();

    public static String execute(ControlPoint controlPoint) {
        try {
            String cmd = "/bin/bash " + controlPoint.getReference()+" " + controlPoint.getParameters();
            cmd = cmd.replaceAll("(?s)\\<.*?\\>", "");
            cmd = cmd.replaceAll("(?s)\\[.*?\\]", "").trim();
            logger.debug("Running command: " + cmd);
            Process p = Runtime.getRuntime().exec(cmd);

            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuffer sb = new StringBuffer();
            System.out.println(cmd);
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            logger.debug("Command output: {}", sb.toString().trim());
            return sb.toString().trim();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    
    
}
