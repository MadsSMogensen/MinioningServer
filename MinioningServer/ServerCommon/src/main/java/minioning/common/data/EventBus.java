package minioning.common.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Mogensen
 */
public class EventBus {
    
    private static EventBus instance = null;
    private Map<UUID, Event> bus = new ConcurrentHashMap<UUID, Event>();
    
    public EventBus(){
        
    }
    
    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }
    
    public Map<UUID, Event> getBus(){
        return bus;
    }
    
    public int size(){
        return bus.size();
    }
    
    public void putEvent(Events eventType, String[] data){
        Event newEvent = new Event(eventType, data);
        bus.putIfAbsent(UUID.randomUUID(), newEvent);
    }
    
    public void putEvent(Event event){
        bus.putIfAbsent(UUID.randomUUID(), event);
    }
    
//    public static void removeEvent(UUID ID){
//        bus.remove(ID);
//    }
//    
//    public static Map<UUID, Event> getEventBus(){
//        return bus;
//    }
    
}
