package minioning.movement;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.Event;
import static minioning.common.data.Events.*;
import minioning.common.data.GameData;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mogensen
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class MovementProcessor implements IEntityProcessingService {

    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> entities, Entity entity) {
//////        float dt = GameData.getDt();
//////        
//////        //check for new movement calls
//////        for (Entry<UUID, Event> eventEntry : eventBus.entrySet()) {
//////            UUID key = eventEntry.getKey();
//////            Event event = eventEntry.getValue();
//////            switch (event.getType()) {
//////                case MOVEMENT:  //currently only setting destination x and y
//////                    System.out.println("dx,dy before: " + entity.getDx() + "," + entity.getDy());
//////                    String[] data = event.getData();
//////                    UUID owner = UUID.fromString(data[2]);
//////                    int dx = Integer.parseInt(data[4]);
//////                    int dy = Integer.parseInt(data[5].trim());
//////                    if (entity.getOwner().equals(owner)) {
//////                        entity.setDx(dx);
//////                        entity.setDy(dy);
//////                    }
//////                    System.out.println("dx,dy after : " + entity.getDx() + "," + entity.getDy());
//////                    eventBus.remove(key);
//////                    break;
//////            }
//////        }
//////        
//////        //Moves entities with a destination x,y != current x,y
//////        processRotation(entity);
//////        processMovement(entity, dt);
////////        setEntityMovement(entity);
    }
    
    private void processRotation(Entity entity){
        int x = entity.getX();
        int y = entity.getY();
        int destinationX = entity.getDestinationX();
        int destinationY = entity.getDestinationY();
        
        //get angle
        float direction = getDirection(x, y, destinationX, destinationY);
        //rotate left or right
        
        
        
        
        
    }
    
    
    
    private void processMovement(Entity entity, float dt){
        
    }
    
    

    private void setEntityMovement(Entity entity) {
        if (!destinationReached(entity)) {
            int x = entity.getX();
            int dx = entity.getDx();
            int y = entity.getY();
            int dy = entity.getDy();
            float speed = entity.getCSpeed();
            float direction;
            float xSpeed = entity.getxSpeed();
            float ySpeed = entity.getySpeed();

            //calculate angle
            direction = getDirection(x, dx, y, dy);

            //Directional moving
//            xSpeed = (float) Math.sin(direction) * speed;
//            ySpeed = (float) Math.cos(direction) * speed;
            //simpler moving
            if (x < dx) {
                xSpeed = entity.getCSpeed();
            }
            if (x > dx) {
                xSpeed = -entity.getCSpeed();
            }
            if (y < dy) {
                ySpeed = entity.getCSpeed();
            }
            if (y > dy) {
                ySpeed = -entity.getCSpeed();
            }
            
            
//            System.out.println("xs: " + xSpeed);
//            System.out.println("ys: " + ySpeed);

//            System.out.println("speed: " + xSpeed + ", " + ySpeed);
//System.out.println("pos: " + x + "; " + y);
            entity.setxSpeed(xSpeed);
            entity.setySpeed(ySpeed);
            moveEntity(entity);
        }
    }

    private boolean destinationReached(Entity entity) {
        return widthReached(entity) && heightReached(entity);
    }
    private boolean widthReached(Entity entity){
        int x = entity.getX();
        int dx = entity.getDx();
        float offset = entity.getCSpeed();
        if (x > dx - offset && x < dx + offset) {
            return true;
        }
        return false;
    }
    private boolean heightReached(Entity entity){
        int y = entity.getY();
        int dy = entity.getDy();
        float offset = entity.getCSpeed();
        if (y > dy - offset && y < dy + offset) {
            return true;
        }
        return false;
    }

    private void moveEntity(Entity entity) {
        if (!widthReached(entity)) {
            int x = entity.getX();
            float xSpeed = entity.getxSpeed();
            x += xSpeed;
            entity.setX(x);
        }
        if(!heightReached(entity)){
            int y = entity.getY();
            float ySpeed = entity.getySpeed();
            y += ySpeed;
            entity.setY(y);
        }
    }

    public float getDirection(float x1, float x2, float y1, float y2) {
        float direction = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
        direction = direction % 360;

        if (direction < 0) {
            direction += 360;
        }
//        if (direction > 360) {
//            direction -= 360;
//        }
//        System.out.println("direction: " + direction);
        return direction;
    }

}
