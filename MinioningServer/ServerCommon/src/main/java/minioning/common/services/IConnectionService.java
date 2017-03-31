package minioning.common.services;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.Event;


/**
 *
 * @author Helle
 */
public interface IConnectionService {
    
    void process(ConcurrentHashMap<UUID, Event> eventBus, ConcurrentHashMap<UUID, Entity> world);
}
