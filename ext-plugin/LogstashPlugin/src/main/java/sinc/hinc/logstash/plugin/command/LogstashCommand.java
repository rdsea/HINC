package sinc.hinc.logstash.plugin.command;

import java.util.Map;

public abstract class LogstashCommand {

    protected Map<String, String> parameters;

    public LogstashCommand() {
    }

    public LogstashCommand(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public abstract String controlActionString();
    public abstract void execute();
    public abstract LogstashCommand createInstance(Map<String, String> parameters);

}
