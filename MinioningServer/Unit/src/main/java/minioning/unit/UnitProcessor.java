package minioning.unit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import static minioning.common.data.EntityType.*;
import minioning.common.data.Event;
import minioning.common.data.GameData;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Helle
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class UnitProcessor implements IEntityProcessingService {
    float standardSpawnDelay = 10;
    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> entities, Entity entity) {
        //check monster spawners, if they should spawn new monsters
        if(entity.getType().equals(PORTAL)){
            if(entity.getSpawnCount() < entity.getMaxMinions()){
                float spawnTimer = entity.getSpawnTimer();
                spawnTimer += GameData.getDt();
                entity.setSpawnTimer(spawnTimer);
                if(spawnTimer >= standardSpawnDelay){
                    spawnNewMonster(entity, entities);
                    entity.setSpawnCount(entity.getSpawnCount()+1);
                    entity.setSpawnTimer(0);
                }
            }
        }
        //remove entities outside the gameworld
        if(entity.getX() > GameData.getGameWidth()+5 || entity.getY() > GameData.getGameHeight()+5){
            entities.remove(entity.getID());
        }
    }
    
    private void spawnNewMonster(Entity entity, Map<UUID, Entity> entities){
        Entity newMonster = new Entity(entity.getID(), entity.getMinionType(), entity.getX()+30, entity.getY()+30);
        newMonster.setLocation(entity.getLocation());
        newMonster.setDx(newMonster.getX());
        newMonster.setDy(newMonster.getY());
        entities.put(newMonster.getID(), newMonster);
        System.out.println("");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("MONSTER SPAWNED!");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("");
    }
}
