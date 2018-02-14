package sinc.hinc.logstash.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.logstash.plugin.command.*;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class LogstashAdapter implements ProviderQueryAdaptor<LogstashItem> {

    private LogstashCommandFactory logstashCommandFactory;
    private static Logger logger = LoggerFactory.getLogger(LogstashAdapter.class.getSimpleName());

    public LogstashAdapter(){
        logstashCommandFactory = new LogstashCommandFactory();
        logstashCommandFactory.registerCommand(new StartCommand());
        logstashCommandFactory.registerCommand(new StopCommand());
        logstashCommandFactory.registerCommand(new EditConfigCommand());
    }


    @Override
    public Collection<LogstashItem> getItems(Map<String, String> settings) {

        logger.info("get logstash items");

        ArrayList<LogstashItem> logstashItems = new ArrayList<>();
        //read config-file located at settings.endpoint(?)
        //create item from config
        //return item

        //TODO

        logger.info("getItems.settings:");
        for(String key: settings.keySet()){
            logger.info(key + ": " + settings.get(key));
        }

        LogstashItem dummy = new LogstashItem();
        dummy.setInput("dummyInput");
        dummy.setFilter("dummyFilter");
        dummy.setOutput("dummyOutput");
        logstashItems.add(dummy);


        return logstashItems;
    }

    @Override
    public void sendControl(String controlAction, Map<String, String> parameters) {
        //Controls:
        //* start logstash
        //* change config
        //* stop logstash

        LogstashCommand command = logstashCommandFactory.createCommand(controlAction, parameters);
        command.execute();
    }

    @Override
    public ResourcesProvider getProviderAPI(Map<String, String> settings) {

        //return info about Controls?
        return new ResourcesProvider();
    }

    @Override
    public String getName() {
        return "logstashPlugin";
    }
}
