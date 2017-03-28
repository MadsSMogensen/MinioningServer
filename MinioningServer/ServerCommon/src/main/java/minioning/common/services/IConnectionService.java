package minioning.common.services;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.EventBus;


/**
 *
 * @author Helle
 */
public interface IConnectionService {
    
    void process(EventBus eventBus, ConcurrentHashMap<UUID, Entity> world);
}
