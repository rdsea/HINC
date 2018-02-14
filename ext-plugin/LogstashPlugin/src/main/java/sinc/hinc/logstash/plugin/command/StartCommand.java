package sinc.hinc.logstash.plugin.command;

import java.util.Map;

public class StartCommand extends LogstashCommand {

    public StartCommand(){
    }

    public StartCommand(Map<String, String> parameters){
        super(parameters);
    }


    @Override
    public String controlActionString() {
        return "start";
    }

    @Override
    public void execute() {
        //TODO
    }

    @Override
    public LogstashCommand createInstance(Map<String, String> parameters) {
        return new StartCommand(parameters);
    }
}
