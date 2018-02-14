package sinc.hinc.logstash.plugin.command;

import java.util.Map;

public class EditConfigCommand extends LogstashCommand {
    public EditConfigCommand() {
    }

    public EditConfigCommand(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public String controlActionString() {
        return "editConfig";
    }

    @Override
    public void execute() {
        //TODO
    }

    @Override
    public LogstashCommand createInstance(Map<String, String> parameters) {
        return new EditConfigCommand(parameters);
    }
}
