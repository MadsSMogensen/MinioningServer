package minioning.unit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import static minioning.common.data.EntityType.*;
import minioning.common.data.Event;
import static minioning.common.data.Events.*;
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
                    spawnNewMonster(entity, eventBus);
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
    
    private void spawnNewMonster(Entity owner, ConcurrentHashMap<UUID, Event> eventBus){
        String data = "";
        data += owner.getID() + ";";
        data += owner.getMinionType() + ";";
        data += owner.getX() + ";";
        data += owner.getY() + ";";
        data += owner.getDx() + ";";
        data += owner.getDy() + ";";
        data += owner.getLocation();
        String[] dataArray = data.split(";");
        Event newEvent = new Event(CREATEMONSTER, dataArray);
        eventBus.putIfAbsent(UUID.randomUUID(), newEvent);
        
    }
}
