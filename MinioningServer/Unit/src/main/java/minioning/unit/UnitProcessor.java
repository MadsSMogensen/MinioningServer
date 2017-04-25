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
        if(entity.getType().equals(ENEMY)){
            Entity Player;
            System.out.println("enemy found");
            System.out.println(entity.getX() + "," + entity.getY());
            for(Map.Entry<UUID, Entity> entry : entities.entrySet()){
                Entity entryEntity = entry.getValue();
                if(entryEntity.getType().equals(PLAYER)){
                    System.out.println("enemy found player");
                    int entryx = entryEntity.getX();
                    int entryy = entryEntity.getY();
                    int x = entity.getX();
                    int y = entity.getY();
                    System.out.println("distance to player: " + distance(x,y,entryx,entryy));
                    if(distance(x,y,entryx,entryy) <= 100){
                        System.out.println("setting movement to players position");
                        entity.setDx(entryx);
                        entity.setDy(entryy);
                    }
                }
            }
        }
        
        
        //remove entities outside the gameworld
        if(entity.getX() > GameData.getGameWidth()+5 || entity.getY() > GameData.getGameHeight()+5){
            entities.remove(entity.getID());
            System.out.println("entity out of bounds removed");
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
    
    private float distance(int x1, int y1, int x2, int y2){
        float length = 0;
        //sqrt(a^2+b^2)
        length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        return length;
    }
}
