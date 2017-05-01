package minioning.movement;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import static minioning.common.data.EntityType.*;
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
        //sets the goal coordinates for the entity
        processEventBus(eventBus, entity);

        //moves the unit according to preevaluated movement
        if (!entity.isImmobile()) {
            processMove(entity);
            processNextMovement(entity, elapsed);
        }
    }

    private void processEventBus(ConcurrentHashMap<UUID, Event> eventBus, Entity entity) {
        for (Entry<UUID, Event> entry : eventBus.entrySet()) {
            UUID key = entry.getKey();
            Event event = entry.getValue();
            String[] data = event.getData();
            if (event.getType().equals(MOVEMENT)) {
                UUID owner = UUID.fromString(data[2]);
                if (owner.equals(entity.getOwner())) {
                    int xGoal = Integer.parseInt(data[4]);
                    int yGoal = Integer.parseInt(data[5]);
                    entity.setDx(xGoal);
                    entity.setDy(yGoal);
                    eventBus.remove(key);
                }
            }
        }
    }

    //set movement til nextMovement
    private void processNextMovement(Entity entity, float elapsed) {
        //get the data
        float speed = entity.getSpeed();
        int x = entity.getX();
        int y = entity.getY();
        float xReal = entity.getxReal();
        float yReal = entity.getyReal();
        int nextX;
        int nextY;
        float nextxReal = xReal;
        float nextyReal = yReal;
        int goalx = entity.getDx();
        int goaly = entity.getDy();
        Vector2D velocity;

        if (entity.getType().equals(HOLYBOLT)) {
            velocity = entity.getVelocity();
            //calculate next x & y position
            nextxReal += velocity.getX() * elapsed;
            nextyReal += velocity.getY() * elapsed;
            nextX = Math.round(xReal);
            nextY = Math.round(yReal);
            //set next x & y position
            entity.setNextx(nextX);
            entity.setNexty(nextY);
            entity.setNextxReal(nextxReal);
            entity.setNextyReal(nextyReal);
        } else {
            //calculate directional unit vector
            Vector2D direction = getDirection(x, goalx, y, goaly);
            entity.setDirection(direction);
            //calculate velocity vector (direction * speed)
            velocity = direction.times(speed);
            entity.setVelocity(velocity);

            //check if goal is reached
            if (!goalReached(entity, x, y, goalx, goaly)) {
                //calculate next x & y position
                nextxReal += velocity.getX() * elapsed;
                nextyReal += velocity.getY() * elapsed;
                nextX = Math.round(xReal);
                nextY = Math.round(yReal);
                //set next x & y position
                entity.setNextx(nextX);
                entity.setNexty(nextY);
                entity.setNextxReal(nextxReal);
                entity.setNextyReal(nextyReal);
            }
        }
    }

    private boolean goalReached(Entity entity, int x, int y, int goalx, int goaly) {
        return distance(x, y, goalx, goaly) < entity.getSize();
    }

    private float distance(int x1, int y1, int x2, int y2) {
        float a = (x2 - x1) * (x2 - x1);
        float b = (y2 - y1) * (y2 - y1);
        float distance = (float) Math.sqrt(a + b);
        return distance;
    }

    private void processMove(Entity entity) {
        //get the data
        int x = entity.getNextx();
        int y = entity.getNexty();
        float xReal = entity.getNextxReal();
        float yReal = entity.getNextyReal();
        //set the new position
        entity.setX(x);
        entity.setY(y);
        entity.setxReal(xReal);
        entity.setyReal(yReal);
    }
}
