package minioning.movement;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import static minioning.common.data.EntityType.PLAYER;
import minioning.common.data.Event;
import static minioning.common.data.Events.*;
import static minioning.common.data.GameData.getDt;
import minioning.common.data.Vector2D;
import static minioning.common.data.Vector2D.getDirection;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mogensen
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class MovementProcessor implements IEntityProcessingService {
    
//    float lastTime = 0;
    float elapsed;
    
    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> entities, Entity entity) {
        elapsed = getDt();
        //check the eventBus for movement events
        for (Entry<UUID, Event> entry : eventBus.entrySet()) {
            UUID key = entry.getKey();
            Event event = entry.getValue();
            String[] data = event.getData();
            if (event.getType().equals(MOVEMENT)) {
                UUID owner = UUID.fromString(data[2]);
                if (owner.equals(entity.getOwner())) {
                    int xGoal = Integer.parseInt(data[4]);
                    int yGoal = Integer.parseInt(data[5]);
                    /* OLD VECTOR MOVEMENT
                    Vector2D vGoal = new Vector2D(x, y);
                    entity.setvGoal(vGoal);
                    */
                    entity.setDx(xGoal);
                    entity.setDy(yGoal);
                    eventBus.remove(key);
                }
            }
        }
        if (entity.getType().equals(PLAYER)) {
            processMovement(entity, elapsed);
        }
    }
    /* OLD PROCESSMOVEMENT
    private void processMovement(Entity entity, float elapsed) {
        Vector2D currentPos = entity.getvPosition();
        Vector2D goalPos = entity.getvGoal();
        float speed = entity.getSpeed();
        
        float distance = Vector2D.distance(currentPos, goalPos);
        Vector2D direction = goalPos.minus(currentPos);
        direction.normalize();
        if(distance < 0){
           distance *= -1;
        }
        if (distance > 3 || distance < -3) {
            Vector2D newPosition;
            newPosition = direction.times(speed).times(elapsed);
            entity.setvPosition(newPosition);
            
            entity.setVx(Math.round(entity.getVx() + newPosition.getX()));
            
            entity.setVy(Math.round(entity.getVy() + newPosition.getY()));
        }
    }*/
    //mangler offset på ca 5 pixels hver retning!
    private void processMovement(Entity entity, float elapsed) {
        //get data
        float speed = entity.getSpeed();
        int x = entity.getX();
        int y = entity.getY();
        float xReal = entity.getxReal();
        float yReal = entity.getyReal();
        int gx = entity.getDx();
        int gy = entity.getDy();
        //calculate directional unit vector
        Vector2D direction = getDirection(x,gx,y,gy);
        entity.setDirection(direction);
        //calculate velocity vector (direction*speed)
        Vector2D velocity = direction.times(speed);
        entity.setVelocity(velocity);
        //calculate new x & y position
        xReal += velocity.getX() * elapsed;
        yReal += velocity.getY() * elapsed;
        x = Math.round(xReal);
        y = Math.round(yReal);
        //set the new position
        entity.setX(x);
        entity.setY(y);
        entity.setxReal(xReal);
        entity.setyReal(yReal);
    }
}
