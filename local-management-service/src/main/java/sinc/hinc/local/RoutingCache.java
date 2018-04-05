package sinc.hinc.local;

import sinc.hinc.common.communication.HincMessage;

import java.util.concurrent.ConcurrentHashMap;

public class RoutingCache {

    private ConcurrentHashMap<String, HincMessage.HincMessageDestination> destinations;
    private static RoutingCache routingCache;

    protected RoutingCache(){
        this. destinations = new ConcurrentHashMap<>();
    }

    public static RoutingCache getInstance(){
        if(routingCache == null){
            routingCache = new RoutingCache();
        }

        return routingCache;
    }

    public void put(String key, HincMessage.HincMessageDestination destination){
        this.destinations.put(key, destination);
    }

    public HincMessage.HincMessageDestination get(String key){
        HincMessage.HincMessageDestination destination = this.destinations.get(key);
        this.destinations.remove(key);
        return destination;
    }

    public boolean contains(String key){
        return this.destinations.contains(key);
    }
}
