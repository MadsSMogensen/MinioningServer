package minioning.unit;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.EntityType;
import static minioning.common.data.EntityType.*;
import minioning.common.data.Event;
import static minioning.common.data.Events.*;
import minioning.common.data.Location;
import minioning.common.data.Vector2D;
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
            if(event.getType().equals(SKILLQ)){
                System.out.println("SKILLQ found");
                createSkill(event, entities);
                eventBus.remove(key);
            }
            if(event.getType().equals(CREATEMONSTER)){
                System.out.println("CREATEMONSTER found");
                createMonster(event, entities);
                eventBus.remove(key);
            }
        }
    }
    
    private void createSkill(Event event, Map<UUID, Entity> entities){
        String[] data = event.getData();
//        System.out.println("creating skill with data:");
//        for(int i = 0; i < data.length; i++){
//            System.out.println(data[i]);
//        }
        UUID owner = UUID.fromString(data[2]);
        Entity ownerEntity = getOwnerEntity(owner, entities);
        Entity skillEntity = new Entity(ownerEntity.getID(), "", ownerEntity.getX(), ownerEntity.getY());
        skillEntity.setLocation(ownerEntity.getLocation());
        skillEntity.setType(EntityType.HOLYBOLT);
        skillEntity.setSpeed(200);
        
        Vector2D direction = Vector2D.getDirection(skillEntity.getX(), Integer.parseInt(data[4]), skillEntity.getY(), Integer.parseInt(data[5]));
        Vector2D velocity = direction.times(skillEntity.getSpeed());
        skillEntity.setVelocity(velocity);
        entities.putIfAbsent(skillEntity.getID(), skillEntity);
    }

    private void createPlayer(Event event, Map<UUID, Entity> entities) {
        String[] data = event.getData();
        for (String data1 : data) {
            System.out.println(data1);
        }
        UUID owner = UUID.fromString(data[2]);
        String name = data[4];
        Entity newEntity = new Entity(owner, name);
        newEntity.setLocation(Location.wilderness);
        entities.put(newEntity.getID(), newEntity);
        System.out.println("Player created!");
    }
    
    private void createMonster(Event event, Map<UUID, Entity> entities){
        String[] data = event.getData();
        UUID owner = UUID.fromString(data[0]);
        String name = data[1];
        int x = Integer.parseInt(data[2]);
        int y = Integer.parseInt(data[3]);
        int dx = Integer.parseInt(data[4]);
        int dy = Integer.parseInt(data[5]);
        Entity newMonster = new Entity(owner, name, x, y);
        newMonster.setDx(x);//dx
        newMonster.setDy(y);//dy
        newMonster.setLocation(Location.valueOf(data[6]));
        newMonster.setType(ENEMY);
        entities.putIfAbsent(newMonster.getID(), newMonster);
        System.out.println("new monster created");
    }
    
    
    
    private Entity getOwnerEntity(UUID ownerID, Map<UUID, Entity> world){
        Entity entity;
        for(Entry<UUID, Entity> entry : world.entrySet()){
            UUID key = entry.getKey();
            entity = entry.getValue();
            if(entity.getOwner().equals(ownerID)){
                return entity;
            }
        }
        return null;
    }
    
    private Vector2D direction(Entity skillEntity, float dx, float dy){
        Vector2D velocity = new Vector2D();
        /*
        Use the on in Vector2D.class!
        */
        return velocity;
    }
}
