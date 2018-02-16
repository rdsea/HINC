package sinc.hinc.local;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessStorage {

    private Map<String, Process> processMap = new ConcurrentHashMap<>();
    private static ProcessStorage processStorage;

    private ProcessStorage(){
    }


    public synchronized static ProcessStorage getInstance(){
        if(processStorage==null){
            processStorage = new ProcessStorage();
        }
        return processStorage;
    }


    public Process putProcess(String key, Process process){
        return processMap.put(key, process);
    }

    public Process getProcess(String key){
        return processMap.get(key);
    }

    public Process removeProcess(String key){
        return processMap.remove(key);
    }

}
