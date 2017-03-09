package minioning.movement;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import minioning.common.data.Entity;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import static minioning.common.data.Events.*;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mogensen
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class MovementProcessor implements IEntityProcessingService {

    @Override
    public void process(EventBus events, Map<UUID, Entity> entities, Entity entity) {
        for (Entry<UUID, Event> eventEntry : events.getBus().entrySet()) {
            UUID key = eventEntry.getKey();
            Event event = eventEntry.getValue();
            switch (event.getType()) {
                case MOVEMENT:  //currently only setting destination x and y
                    System.out.println("dx,dy before: " + entity.getDx() + "," + entity.getDy());
                    String[] data = event.getData();
                    UUID owner = UUID.fromString(data[0]);
                    int dx = Integer.parseInt(data[1]);
                    int dy = Integer.parseInt(data[2]);
                    if (entity.getOwner().equals(owner)) {
                        entity.setDx(dx);
                        entity.setDy(dy);
                    }
                    System.out.println("dx,dy after : " + entity.getDx() + "," + entity.getDy());
                    break;
            }
        }
    }
}