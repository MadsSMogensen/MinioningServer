package minioning.common.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Mogensen
 */
public class EventBus {
    
    private static EventBus instance = null;
    private static Map<UUID, Event> bus = new ConcurrentHashMap<UUID, Event>();
    
    public EventBus(){
        
    }
    
    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }
    
    public void putEvent(Events event, String[] data){
        Event newEvent = new Event(event, data);
        bus.put(UUID.randomUUID(), newEvent);
    }
    
    public static void removeEvent(UUID ID){
        bus.remove(ID);
    }
    
    public static Map<UUID, Event> getEventBus(){
        return bus;
    }
    
}
