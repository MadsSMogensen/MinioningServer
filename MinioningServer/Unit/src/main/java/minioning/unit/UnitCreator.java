package minioning.unit;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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
public class UnitCreator implements IEntityCreatorService {

    @Override
    public void createNew(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> entities) {
//        System.out.println(events.getBus().size());
//        System.out.println(EventBus.getInstance().size());
        for (Entry<UUID, Event> entry : eventBus.entrySet()) {
            UUID key = entry.getKey();
            Event value = entry.getValue();
            if (value.getType().equals(CREATEPLAYER)) {
                System.out.println("CREATEPLAYER found");
                createPlayer(value, entities);
                eventBus.remove(key);
            }
        }
    }

    private void createPlayer(Event value, Map<UUID, Entity> entities) {
        String[] data = value.getData();
        data[0] = data[0];
        for(int i = 0; i < data.length; i++){
            System.out.println(data[i]);
        }
        System.out.println(data[1]);
        System.out.println(data[2]);
        System.out.println(data[3]);
        UUID owner = UUID.fromString(data[2]);
        String name = data[4];
        Entity newEntity = new Entity(owner, name);
        entities.put(newEntity.getID(), newEntity);
        System.out.println("Player created!");
    }
}
