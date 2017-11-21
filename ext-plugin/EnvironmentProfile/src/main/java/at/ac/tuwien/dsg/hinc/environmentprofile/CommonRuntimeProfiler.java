/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.environmentprofile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.abstraction.transformer.ExecutionEnvironmentTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

/**
 *
 * @author hungld
 */
public class CommonRuntimeProfiler implements ProviderQueryAdaptor<ExecutionEnvironment>, ExecutionEnvironmentTransformer<ExecutionEnvironment> {

    @Override
    public Collection<ExecutionEnvironment> getItems(Map<String, String> settings) {
        List<ExecutionEnvironment> allEnv = new ArrayList<>();

        // get system runtime
        ExecutionEnvironment systemRuntime = new ExecutionEnvironment();
        systemRuntime.setName("sh");
        systemRuntime.setDescription("System runtime environment");
        systemRuntime.hasAttribute("os", System.getProperty("os.name"))
                .hasAttribute("arc", System.getProperty("os.arch"))
                .hasAttribute("version", System.getProperty("os.version"))
                .hasAttribute("username", System.getProperty("user.name"))
                .hasAttribute("hostname", getComputerName())
                .hasAttribute("ram", getRAM() + "")
                .hasAttribute("cores", Runtime.getRuntime().availableProcessors() + "");
        allEnv.add(systemRuntime);

        // get Java runtime
        ExecutionEnvironment jre = new ExecutionEnvironment();
        jre.setName("jre");
        jre.setDescription("Java Runtime Environment");
        jre.hasAttribute("version", System.getProperty("java.version"))
                .hasAttribute("home", System.getProperty("java.home"))
                .hasAttribute("vendor", System.getProperty("java.vendor"));
        allEnv.add(jre);

        // get Python runtime
        ExecutionEnvironment python = new ExecutionEnvironment();
        String pythonPath = exec("/usr/bin/which python");
        if (pythonPath != null && !pythonPath.isEmpty()) {
            python.setName("python");
            python.setDescription("Python runtime");
            python.hasAttribute("version", exec("python --version"))
                    .hasAttribute("home", pythonPath)
                    .hasAttribute("path", System.getenv().get("PYTHONPATH"));
            allEnv.add(python);

        }
        return allEnv;
    }

    @Override
    public void sendControl(String controlAction, Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static public String getComputerName() {
        Map<String, String> env = System.getenv();

        if (env.containsKey("COMPUTERNAME")) {
            return env.get("COMPUTERNAME");
        } else if (env.containsKey("HOSTNAME")) {
            return env.get("HOSTNAME");
        } else if (env.containsKey("USERNAME")) {
            return env.get("USERNAME") + "-PC";
        } else {
            return "Unknown Computer";
        }
    }

    static public StringBuilder getRAM() {
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long freeMemory = runtime.freeMemory();
        sb.append(format.format(freeMemory / 1024));
        return sb;
    }

    private String exec(String command) {;
        try {
            Process p = Runtime.getRuntime().exec(command);

            p.waitFor();
            if (p.exitValue() != 0) {
                return null;
            }
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }


   

    @Override
    public String getName() {
        return "runtime";
    }

    @Override
    public ExecutionEnvironment updateExecutionEnvironment(ExecutionEnvironment data) {
        return null;
    }

    @Override
    public ResourcesProvider getProviderAPI(Map<String, String> settings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
