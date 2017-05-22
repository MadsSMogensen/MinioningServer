package minioning.common.data;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Jakob and Mads
 */
public class EventBus {

    private static ConcurrentHashMap<UUID, Event> bus;

    /**
     * Constructor method for EventBus
     */
    private EventBus() {
        bus = new ConcurrentHashMap<UUID, Event>();
    }

    /**
     *
     * @return The concurrentHashMap bus
     */
    public static ConcurrentHashMap<UUID, Event> getBus() {
        if (bus == null) {
            bus = new ConcurrentHashMap<UUID, Event>();
        }
        return bus;
    }

    /**
     *
     * @return The size of the ConcurrentHashMap
     */
    public static int size() {
        return getBus().size();
    }

    /**
     *
     * @param eventType Enum of type eventType
     * @param data a String array of data
     */
    public static void putEvent(Events eventType, String[] data) {
        Event newEvent = new Event(eventType, data);
        getBus().putIfAbsent(UUID.randomUUID(), newEvent);
    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     */
    public static void putEvent(Event event) {
        getBus().putIfAbsent(UUID.randomUUID(), event);
    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     */
    public static void removeEvent(UUID event) {
        getBus().remove(event);
    }
}
