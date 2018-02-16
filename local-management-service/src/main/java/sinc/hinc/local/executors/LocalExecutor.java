/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.executors;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.payloads.ControlResult;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

/**
 *
 * @author hungld
 */
public class LocalExecutor implements ExecutorsInterface {

    static Logger logger = HincConfiguration.getLogger();

    @Override
    public ControlResult execute(ControlPoint controlPoint) {
        try {
            long startTime = new Date().getTime();
            String cmd = "/bin/bash " + controlPoint.getReference() + " " + (controlPoint.getParameters()==null?"":controlPoint.getParameters());
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
            logger.debug("Command return code: {}, output: {}", p.exitValue(), sb.toString().trim());
            long endTime = new Date().getTime();
            return new ControlResult(ControlResult.CONTROL_RESULT.SUCCESS, 0, sb.toString().trim()).hasExecutionTime(endTime - startTime);

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            return new ControlResult(ControlResult.CONTROL_RESULT.EXECUTOR_ERROR, 0, ex.getMessage());
        }
    }

}
