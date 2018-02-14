package sinc.hinc.logstash.plugin.command;

import java.util.Map;

public class StopCommand extends LogstashCommand {

    public StopCommand() {
    }

    public StopCommand(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public String controlActionString() {
        return "stop";
    }

    @Override
    public void execute() {
        //TODO
    }

    @Override
    public LogstashCommand createInstance(Map<String, String> parameters) {
        return new StopCommand(parameters);
    }
}
