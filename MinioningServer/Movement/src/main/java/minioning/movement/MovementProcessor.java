package minioning.movement;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import minioning.common.data.Entity;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import static minioning.common.data.Events.*;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mogensen
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class MovementProcessor implements IEntityProcessingService {

    @Override
    public void process(EventBus events, Map<UUID, Entity> entities, Entity entity) {
        for (Entry<UUID, Event> eventEntry : events.getBus().entrySet()) {
            UUID key = eventEntry.getKey();
            Event event = eventEntry.getValue();
            switch (event.getType()) {
                case MOVEMENT:  //currently only setting destination x and y
                    System.out.println("dx,dy before: " + entity.getDx() + "," + entity.getDy());
                    String[] data = event.getData();
                    UUID owner = UUID.fromString(data[0]);
                    int dx = Integer.parseInt(data[1]);
                    int dy = Integer.parseInt(data[2]);
                    if (entity.getOwner().equals(owner)) {
                        entity.setDx(dx);
                        entity.setDy(dy);
                    }
                    System.out.println("dx,dy after : " + entity.getDx() + "," + entity.getDy());
                    break;
            }
            events.getBus().remove(key);
        }

        //Moves entities with a destination x,y != current x,y
        setEntityMovement(entity);
        moveEntity(entity);
    }

    private void setEntityMovement(Entity entity) {
        if (entity.getX() != entity.getDx() || entity.getY() != entity.getDy()) {
            float x = entity.getX();
            float dx = entity.getDx();
            float y = entity.getY();
            float dy = entity.getDy();
            float speed = entity.getSpeed();
            float direction;
            float xSpeed;
            float ySpeed;

            //calculate angle
            
            direction = getDirection(x,dx,y,dy);
            
            //moving
            xSpeed = (float) Math.cos(direction) * speed;
            ySpeed = (float) Math.cos(direction) * speed;

            System.out.println("speed: " + xSpeed + ", " + ySpeed);
            entity.setxSpeed(xSpeed);
            entity.setySpeed(ySpeed);
        }
    }

    private void moveEntity(Entity entity) {
        float x = entity.getX();
        float y = entity.getY();
        float xSpeed = entity.getxSpeed();
        float ySpeed = entity.getySpeed();

        x += xSpeed;
        y += ySpeed;

        entity.setX(x);
        entity.setY(y);
    }

    public float getDirection(float x1, float x2, float y1, float y2) {
        float direction = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

        if (direction < 0) {
            direction += 360;
        }
        if(direction < 360){
            direction -= 360;
        }

        return direction;
    }

}
