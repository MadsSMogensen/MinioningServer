/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.common.services;

import java.util.Map;
import java.util.UUID;
import minioning.common.data.Entity;
import minioning.common.data.EventBus;

/**
 *
 * @author Mogensen
 */
public interface IEntityProcessingService {
    
    void process(EventBus events, Map<UUID, Entity> entities, Entity entity);
    
}
