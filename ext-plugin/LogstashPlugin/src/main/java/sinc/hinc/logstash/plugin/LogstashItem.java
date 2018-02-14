package sinc.hinc.logstash.plugin;

import sinc.hinc.model.Extensible.ExtensibleModel;

import java.io.File;

public class LogstashItem extends ExtensibleModel{
    private String input;
    private String filter;
    private String output;

    public LogstashItem(){
        super(LogstashItem.class);
    }

    public LogstashItem(String input, String filter, String output) {
        super(LogstashItem.class);
        this.input = input;
        this.filter = filter;
        this.output = output;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
