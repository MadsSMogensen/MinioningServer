package minioning.unit;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import static minioning.common.data.EntityType.DOOR;
import static minioning.common.data.EntityType.ENEMY;
import static minioning.common.data.EntityType.LAVA;
import static minioning.common.data.EntityType.PLAYER;
import static minioning.common.data.EntityType.PORTAL;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import static minioning.common.data.Events.CREATEPLAYER;
import minioning.common.data.Location;
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
            Event event = entry.getValue();
            if (event.getType().equals(CREATEPLAYER)) {
                System.out.println("CREATEPLAYER found");
                createPlayer(event, entities);
                eventBus.remove(key);
            }
        }
        createEnemy(entities);
    }

    private void createPlayer(Event event, Map<UUID, Entity> entities) {
        String[] data = event.getData();
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }
        UUID owner = UUID.fromString(data[2]);
        String name = data[4];
        Entity newEntity = new Entity(owner, name);
        newEntity.setLocation(Location.wilderness);
        entities.put(newEntity.getID(), newEntity);
        System.out.println("Player created!");
    }

    private void createEnemy(Map<UUID, Entity> entities) {

        for (Entity entity : entities.values()) {

            if (entity.getType() == PLAYER) {
                if (entity.getSpawnCount() < 1) {
                    int x = 100;
                    int y = 100;

                    UUID owner = UUID.randomUUID();
                    String name = "Enemy";
                    Entity newEntity = new Entity(owner, name);
                    newEntity.setX(x);
                    newEntity.setY(y);
                    newEntity.setType(LAVA);
                    newEntity.setLocation(entity.getLocation());
                    entities.put(owner, newEntity);
                    entity.setSpawnCount(1);
                    System.out.println("Enemy created");
                }
            }
        }
    }
}
