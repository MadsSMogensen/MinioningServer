package minioning.health;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.EntityType;
import static minioning.common.data.EntityType.MINION;
import static minioning.common.data.EntityType.PLAYER;
import minioning.common.data.Event;
import static minioning.common.data.Events.HPCHANGE;
import minioning.common.data.Location;
import minioning.common.services.IHealthProcessorService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jakob and Mads
 */
@ServiceProvider(service = IHealthProcessorService.class)
public class HealthProcess implements IHealthProcessorService {

    private Random ran = new Random();

    /**
     * This method can process entities based on events
     *
     * @param eventBus HashMap with all events needing handling on the server
     * @param world HashMap of every entity
     */
    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, ConcurrentHashMap<UUID, Entity> world) {

        // Checks for events of the HPCHANGE type
        for (Map.Entry<UUID, Event> entry : eventBus.entrySet()) {
            UUID key = entry.getKey();
            Event event = entry.getValue();
            System.out.println("Event found!: " + event.getType());
            String[] data = event.getData();
            if (event.getType() == HPCHANGE) {
                if (EntityType.valueOf(data[1]) == EntityType.HOLYBOLT) {
                    UUID entityID = UUID.fromString(data[0]);
                    Entity entity = world.get(entityID);

                    try {
                        if (!entity.isImmobile()) {
                            entity.setHp(entity.getHp() - calculateDmg(5, 10, ran));

                            // Switch case that handles what happens to an entity with health below zero
                            if (entity.getHp() <= 0) {
                                switch (entity.getType()) {
                                    case PLAYER:
                                        entity.setPosition(100, 100, Location.wilderness);
                                        entity.setxReal(100);
                                        entity.setyReal(100);
                                        entity.setDx(100);
                                        entity.setDy(100);
                                        entity.setHp(100);
                                        break;
                                    case MINION:
                                    case ENEMY:
                                        Entity owner = world.get(entity.getOwner());
                                        owner.setSpawnCount(owner.getSpawnCount() - 1);
                                        world.remove(entityID);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                world.replace(entityID, entity);
                            }
                            System.out.println(entity.getHp());
                        }

                    } catch (Exception e) {
                    }
                    eventBus.remove(key);
                }
            }
        }
    }

    /**
<<<<<<< HEAD:MinioningServer/Health/src/main/java/minioning/health/HealthProcess.java
     * This method calculate the damage mased on a given min and max value
     *
=======
     * This method calculates the damage based on a given min and max value
     * 
>>>>>>> Test:MinioningServer/ServerHealth/src/main/java/minioning/health/HealthProcess.java
     * @param min Minimun damage
     * @param max Maximum damage
     * @param ran Java util Random object
     * @return a int with a random value between min and max
     */
    private int calculateDmg(int min, int max, Random ran) {
        int dmg = ran.nextInt(max - min + 1) + min;
        return dmg;
    }
}
