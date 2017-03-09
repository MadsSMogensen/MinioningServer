package minioning.unit;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import minioning.common.data.Entity;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import static minioning.common.data.Events.CREATEPLAYER;
import minioning.common.services.IEntityCreatorService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Helle
 */
@ServiceProvider(service = IEntityCreatorService.class)
public class UnitCreator implements IEntityCreatorService{

    @Override
    public void createNew(EventBus events, Map<UUID, Entity> entities) {
        for(Entry<UUID, Event> entry : events.getBus().entrySet()){
            UUID key = entry.getKey();
            Event value = entry.getValue();
            if(value.getType().equals(CREATEPLAYER)){
                String[] data = value.getData();
                UUID owner = UUID.fromString(data[0]);
                String name = data[1];
                Entity newEntity = new Entity(owner, name);
                entities.put(UUID.randomUUID(), newEntity);
                events.getBus().remove(key);
                System.out.println("Player created!");
            }
        }
    }
}
