package minioning.common.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Mogensen
 */
public class EventBus {
    
    private static EventBus instance;
    private static ConcurrentHashMap<UUID, Event> bus;
    
    private EventBus(){
        bus = new ConcurrentHashMap<UUID, Event>();
        System.out.println("BUSCREATED");
    }
    
//    public static EventBus getInstance() {
//        if (instance == null) {
//            instance = new EventBus();
//            System.out.println("new instance created");
//        }
//        return instance;
//    }
    
    public static ConcurrentHashMap<UUID, Event> getBus(){
        if(bus == null){
            bus = new ConcurrentHashMap<UUID, Event>();
            System.out.println("new bus instance created");
        }
        return bus;
    }
    
    public static int size(){
        return getBus().size();
    }
    
    public static void putEvent(Events eventType, String[] data){
        Event newEvent = new Event(eventType, data);
        getBus().putIfAbsent(UUID.randomUUID(), newEvent);
    }
    
    public static void putEvent(Event event){
        getBus().putIfAbsent(UUID.randomUUID(), event);
    }
    public static void removeEvent(UUID event){
        getBus().remove(event);
    }
    
//    public static void removeEvent(UUID ID){
//        bus.remove(ID);
//    }
//    
//    public static Map<UUID, Event> getEventBus(){
//        return bus;
//    }
    
}
