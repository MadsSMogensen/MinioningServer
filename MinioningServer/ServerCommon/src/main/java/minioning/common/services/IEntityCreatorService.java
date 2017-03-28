package minioning.common.services;

import java.util.Map;
import java.util.UUID;
import minioning.common.data.Entity;
import minioning.common.data.EventBus;

/**
 *
 * @author Mogensen
 */
public interface IEntityCreatorService {
    
    void createNew(EventBus events, Map<UUID, Entity> entities);
    
}
