/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.movement;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import minioning.common.data.Entity;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import minioning.common.services.IEntityProcessingService;

/**
 *
 * @author Mogensen
 */
public class MovementProcessor implements IEntityProcessingService {

    @Override
    public void process(EventBus events, Map<UUID, Entity> entities, Entity entity) {
        for(Entry<UUID, Event> entry : events.getBus().entrySet()){
            UUID key = entry.getKey();
        }
    }
}
