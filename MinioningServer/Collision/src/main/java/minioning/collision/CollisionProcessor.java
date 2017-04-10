package minioning.collision;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import minioning.common.data.Entity;
import static minioning.common.data.EntityType.DOOR;
import static minioning.common.data.EntityType.PLAYER;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mogensen
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class CollisionProcessor implements IEntityProcessingService {
    
    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> entities, Entity entity) {
        
        float x = entity.getX();
        float y = entity.getY();
//        float xSpeed = entity.getxSpeed();
//        float ySpeed = entity.getySpeed();
        float speed = entity.getCSpeed();
        UUID ID = entity.getID();
        int bounds = 160; //5*32 pixels
        Circle c1 = entity.getBounds();
        for (Entry<UUID, Entity> entry : entities.entrySet()) {
            if (entry.getKey() != ID) {
                float entryX = entry.getValue().getX();
                float entryY = entry.getValue().getY();
                //only look in the same world
                if (entry.getValue().getLocation().equals(entity.getLocation())) {
                    //only look for entities within 5*32 pixels
                    if (entryX <= x + bounds && entryX >= x - bounds && entryY <= y + bounds && entryY >= y - bounds) {
                        Circle c2 = entry.getValue().getBounds();
                        
                        if (collide(c1, c2)) {
                            System.out.println("COLLISION DETECTED!");
                            if (entry.getValue().getType().equals(DOOR) && entity.getType().equals(PLAYER)) {
                                entity.setLocation(entry.getValue().getDoorTo());
                                entity.setX(500);
                                entity.setY(200);
                                entity.setVelocity(null);
                                entity.setDx(500);
                                entity.setDy(200);
                            } else {
                                speed = -speed;
                                entity.setCSpeed(speed);
                            }

//                        entity.setDx(x);
//                        entity.setDy(y);
//                    entity.setxSpeed(xSpeed);
//                    entity.setySpeed(ySpeed);
                        } else if (speed < 0) {
                            speed *= -1;
                            entity.setCSpeed(speed);
                        }
                    }
                }
                
            }
        }
    }
//    //returns true if c1 and c2 collides
//    private boolean collide(Circle c1, Circle c2) {
//        double distanceX = c2.centerXProperty().getValue() - c1.centerXProperty().getValue();
//        double distanceY = c2.centerYProperty().getValue() - c1.centerYProperty().getValue();
//        double radiusSum = c2.getRadius() + c1.getRadius();
//        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;
//    }

    private boolean collide(Shape s1, Shape s2) {
        return s1.intersects(s2.getBoundsInLocal());
    }
    
}
