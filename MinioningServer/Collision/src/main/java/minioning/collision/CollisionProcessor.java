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
import minioning.common.data.GameData;
import minioning.common.data.Vector2D;
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
        UUID ID = entity.getID();
        //don't check immobile entities
        if (!entity.isImmobile()) {
//            System.out.println("checking collision for: " + entity.getName());
            for (Entry<UUID, Entity> entry : entities.entrySet()) {
                //don't check collision with itself
                if (entry.getKey() != ID) {
                    Entity entryEntity = entry.getValue();
                    //Only look in the same world
//                    System.out.println("checking if other entity is in same world");
                    if (entryEntity.getLocation().equals(entity.getLocation())) {
                        //check if colliding
//                        System.out.println("checking if other entity is colliding with the above");
                        if (colliding(entryEntity, entity)) {
                            //collision detected!
//                            System.out.println("collision detected!");
                            int x = entity.getX();
                            int y = entity.getY();
                            int entryx = entryEntity.getX();
                            int entryy = entryEntity.getY();
                            
                            Vector2D collisionVector = new Vector2D(entryx-x, entryy-y);
                            
                            entity.setNextVelocity(entity.getNextVelocity().plus(collisionVector));
                        }else{
//                            System.out.println("not collided!:");
//                            System.out.println("entity: " + entity.getX() + "," + entity.getY() + ", size: " + entity.getSize());
//                            System.out.println("entry : " + entryEntity.getX() + "," + entryEntity.getY() + ", size: " + entryEntity.getSize());
//                            System.out.println("---------------------");
                        }
                    }
                }
            }
        }

//        float x = entity.getX();
//        float y = entity.getY();
////        float xSpeed = entity.getxSpeed();
////        float ySpeed = entity.getySpeed();
//        float speed = entity.getCSpeed();
//        UUID ID = entity.getID();
//        int bounds = 160; //5*32 pixels
//        Circle c1 = entity.getBounds();
//        for (Entry<UUID, Entity> entry : entities.entrySet()) {
//            if (entry.getKey() != ID) {
//                float entryX = entry.getValue().getX();
//                float entryY = entry.getValue().getY();
//                //only look in the same world
//                if (entry.getValue().getLocation().equals(entity.getLocation())) {
//                    //only look for entities within 5*32 pixels
//                    if (entryX <= x + bounds && entryX >= x - bounds && entryY <= y + bounds && entryY >= y - bounds) {
//                        Circle c2 = entry.getValue().getBounds();
//                        
//                        if (collide(c1, c2)) {
//                            System.out.println("COLLISION DETECTED!");
//                            if (entry.getValue().getType().equals(DOOR) && entity.getType().equals(PLAYER)) {
//                                entity.setLocation(entry.getValue().getDoorTo());
//                                entity.setX(500);
//                                entity.setY(200);
//                                entity.setVelocity(null);
//                                entity.setDx(500);
//                                entity.setDy(200);
//                            } else {
//                                speed = -speed;
//                                entity.setCSpeed(speed);
//                            }
//
////                        entity.setDx(x);
////                        entity.setDy(y);
////                    entity.setxSpeed(xSpeed);
////                    entity.setySpeed(ySpeed);
//                        } else if (speed < 0) {
//                            speed *= -1;
//                            entity.setCSpeed(speed);
//                        }
//                    }
//                }
//                
//            }
//        }
    }
    //THIS METHOD IS CALCULATING A WRONG X AND Y! vector might use a full dt, instead of elapsed?
    private boolean colliding(Entity entry, Entity entity) {
        //common data
        float dt = GameData.getDt();
        int bounds;
        
        //current entity data
        int x = entity.getX();
        int y = entity.getY();
        Vector2D velocity = entity.getVelocity();
        float speed = entity.getSpeed();
        float vx = x;
        float vy = y;
        vx += velocity.getX()*dt*speed;
        vy += velocity.getY()*dt*speed;
        float size = entity.getSize();
        
        //current entry data
        int entryx = entry.getX();
        int entryy = entry.getY();
        Vector2D entryVelocity = entry.getVelocity();
        float entrySpeed = entry.getSpeed();
        float entryvx = entryx;
        float entryvy = entryy;
        entryvx += entryVelocity.getX()*dt*entrySpeed;
        entryvy += entryVelocity.getY()*dt*entrySpeed;
        float entrySize = entry.getSize();
        
        //checking vx is within bounds
        float lowerx = entryvx-entrySize;
        float upperx = entryvx+entrySize;
        float lowery = entryvy-entrySize;
        float uppery = entryvy+entrySize;
        float entityLowerx = vy-size;
        float entityUpperx = vy+size;
        float entityLowery = vy-size;
        float entityUppery = vy+size;
        if(entityUpperx >= lowerx && entityLowerx <= upperx){
//            System.out.println("x is within now!");
            //checking vy is within bounds
            if(entityUppery >= lowery && entityLowery <= uppery){
//                System.out.println("y is within now!");
                return true; 
            }
        }
//        System.out.println("checking collision: " + vx + "," + vy + " ; " + entryvx + "," + entryvy);
        return false;
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
