package minioning.common.services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.Event;
import minioning.common.data.EventBus;

/**
 *
 * @author Mogensen
 */
public interface IEntityCreatorService {
    
    void createNew(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> world);
    
}
