package sinc.hinc.logstash.plugin.command;

import java.util.HashMap;
import java.util.Map;

public class LogstashCommandFactory {

    private HashMap<String, LogstashCommand> registry = new HashMap<>();


    public void registerCommand(LogstashCommand logstashCommand) {
        registry.put(logstashCommand.controlActionString(), logstashCommand);

    }

    public LogstashCommand createCommand(String controlAction, Map<String, String> parameters){

        return registry.get(controlAction).createInstance(parameters);
    }

}
