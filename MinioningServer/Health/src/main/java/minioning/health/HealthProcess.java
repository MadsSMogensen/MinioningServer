/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Jakob
 */
@ServiceProvider(service = IHealthProcessorService.class)
public class HealthProcess implements IHealthProcessorService {

    private Random ran = new Random();

    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, ConcurrentHashMap<UUID, Entity> world) {

        //        for (Map.Entry<UUID, Event> entry : eventBus.getBus().entrySet()) {
        for (Map.Entry<UUID, Event> entry : eventBus.entrySet()) {
            UUID key = entry.getKey();
            Event event = entry.getValue();
            System.out.println("Event found!: " + event.getType());
            String[] data = event.getData();
            if (event.getType() == HPCHANGE) {
                if (EntityType.valueOf(data[1]) == EntityType.HOLYBOLT) {
                    UUID entityID = UUID.fromString(data[0]);
                    Entity entity = world.get(entityID);

                    int min = 5;
                    int max = 10;

                    int dmg = ran.nextInt(max - min + 1) + min;

                    entity.setHp(entity.getHp() - dmg);

                    if (entity.getHp() <= 0 && !entity.isImmobile()) {
                        switch (entity.getType()) {
                            case PLAYER:
                                if (!entity.getLocation().equals(Location.wilderness)) {
                                    entity.setPosition(100, 100, Location.wilderness);
                                    entity.setxReal(100);
                                    entity.setyReal(100);
                                    entity.setDx(100);
                                    entity.setDy(100);
                                    entity.setHp(100);
                                }
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
                    eventBus.remove(key);
                }
            }
        }
    }
}
